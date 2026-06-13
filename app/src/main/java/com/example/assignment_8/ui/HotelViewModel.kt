package com.example.assignment_8.ui

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment_8.Data.Hotel
import com.example.assignment_8.Data.Place
import com.example.assignment_8.Data.RetrofitInstance
import com.example.assignment_8.Data.Room
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HotelViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var verificationId: String? = null

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    private val _userPhone = MutableStateFlow("")
    val userPhone: StateFlow<String> = _userPhone.asStateFlow()

    private val _location = MutableStateFlow("")
    val location: StateFlow<String> = _location.asStateFlow()

    private val _checkIn = MutableStateFlow("")
    val checkIn: StateFlow<String> = _checkIn.asStateFlow()

    private val _checkOut = MutableStateFlow("")
    val checkOut: StateFlow<String> = _checkOut.asStateFlow()

    private val _rooms = MutableStateFlow(1)
    val rooms: StateFlow<Int> = _rooms.asStateFlow()

    private val _adults = MutableStateFlow(1)
    val adults: StateFlow<Int> = _adults.asStateFlow()

    private val _children = MutableStateFlow(0)
    val children: StateFlow<Int> = _children.asStateFlow()

    private val _bedFlag = MutableStateFlow(false)
    val bedFlag: StateFlow<Boolean> = _bedFlag.asStateFlow()

    private val _timeLeft = MutableStateFlow(60)
    val timeLeft: StateFlow<Int> = _timeLeft.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private val _selectedHotel = MutableStateFlow<Hotel?>(null)
    val selectedHotel: StateFlow<Hotel?> = _selectedHotel.asStateFlow()

    private val _selectedRoom = MutableStateFlow<Room?>(null)
    val selectedRoom: StateFlow<Room?> = _selectedRoom.asStateFlow()

    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val places: StateFlow<List<Place>> = _places.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        object CodeSent : AuthState()
        object Success : AuthState()
        data class Error(val message: String) : AuthState()
    }

    init {
        applyTestSettings()
        fetchPlaces()
        if (auth.currentUser != null) {
            _authState.value = AuthState.Success
        }
    }

    private fun applyTestSettings() {
        auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
        auth.firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+918469103393", "123456")
    }

    private fun fetchPlaces() {
        viewModelScope.launch {
            try {
                val fetchedPlaces = RetrofitInstance.api.getPlaces()
                _places.value = fetchedPlaces
            } catch (e: Exception) {
                Log.e("HotelViewModel", "Error fetching places", e)
            }
        }
    }

    fun setUserInfo(name: String, email: String, phone: String) {
        _userName.value = name
        _userEmail.value = email
        _userPhone.value = phone
    }

    fun sendOtp(activity: Activity) {
        _authState.value = AuthState.Loading
        
        val digits = _userPhone.value.filter { it.isDigit() }
        val phoneNumber = "+91${digits.takeLast(10)}"

        if (phoneNumber == "+918469103393") {
            Log.d("Auth", "Direct bypass for $phoneNumber")
            verificationId = "test_verification_id"
            _authState.value = AuthState.CodeSent
            startTimer()
            return
        }

        applyTestSettings()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    _authState.value = AuthState.Error(e.localizedMessage ?: "Verification Failed")
                }

                override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                    verificationId = id
                    _authState.value = AuthState.CodeSent
                    startTimer()
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(code: String) {
        if (verificationId == "test_verification_id") {
            if (code == "123456") {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Incorrect test OTP. Use 123456")
            }
            return
        }

        _authState.value = AuthState.Loading
        val credId = verificationId
        if (credId == null) {
            _authState.value = AuthState.Error("Session expired. Try again.")
            return
        }
        val credential = PhoneAuthProvider.getCredential(credId, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error(task.exception?.localizedMessage ?: "Sign In Failed")
            }
        }
    }

    fun logout() {
        auth.signOut()
        _authState.value = AuthState.Idle
        _userName.value = ""
        _userEmail.value = ""
        _userPhone.value = ""
    }

    fun setLocation(loc: String) { _location.value = loc }
    fun setCheckIn(date: String) { _checkIn.value = date }
    fun setCheckOut(date: String) { _checkOut.value = date }
    fun setRooms(newCount: Int) { _rooms.value = newCount }
    fun setAdults(newCount: Int) { _adults.value = newCount }
    fun setChildren(newCount: Int) { _children.value = newCount }
    fun setBedFlag(flag: Boolean) { _bedFlag.value = flag }

    fun setSelectedHotel(hotel: Hotel) {
        _selectedHotel.value = hotel
    }

    fun setSelectedRoom(room: Room) {
        _selectedRoom.value = room
    }

    fun startTimer() {
        if (_isTimerRunning.value) return
        _isTimerRunning.value = true
        _timeLeft.value = 60
        viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000)
                _timeLeft.value -= 1
            }
            _isTimerRunning.value = false
        }
    }
}

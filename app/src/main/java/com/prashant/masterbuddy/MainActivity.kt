package com.prashant.masterbuddy

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.prashant.masterbuddy.utils.Utils
import com.prashant.masterbuddy.ws.JsonServiceOKHTTP
import com.prashant.masterbuddy.ws.JsonServices
import com.prashant.masterbuddy.ws.VolleyCallback
import com.prashant.masterbuddy.ws.model.RegisterResult
import com.prashant.masterbuddy.ws.model.UserResult

class MainActivity : AppCompatActivity() {

    private var edtName: EditText? = null
    private var edtUsername: EditText? = null
    private var edtPassword: EditText? = null
    private var edtEmailId: EditText? = null
    private var edtRegPassword: EditText? = null
    private var llLogin: LinearLayout? = null
    private var llRegister: LinearLayout? = null
    private var btnLogin: Button? = null
    private var btnSignUp: Button? = null
    private var btnSubmit: Button? = null
    private var application: Application? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        application = getApplication() as Application
        edtName = findViewById(R.id.edtName)
        edtEmailId = findViewById(R.id.edtEmailId)
        edtPassword = findViewById(R.id.edtPassword)
        edtRegPassword = findViewById(R.id.edtRegPassword)
        edtUsername = findViewById(R.id.edtUserName)

        llLogin = findViewById(R.id.llLogin)
        llRegister = findViewById(R.id.llRegister)

        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)
        btnSubmit = findViewById(R.id.btnSubmit)

        progressBar = findViewById(R.id.pbLogin)

        application!!.permissionUtils.requestPermission(this)

        btnSignUp!!.setOnClickListener {
            llLogin!!.visibility = View.GONE
            llRegister!!.visibility = View.VISIBLE
            btnSubmit!!.visibility = View.VISIBLE
            btnLogin!!.visibility = View.GONE
            btnSignUp!!.visibility = View.GONE
        }

        btnLogin!!.setOnClickListener {
            // For Login
            val jsonServices = JsonServices(this@MainActivity, object : VolleyCallback {
                override fun starting() {
                    progressBar!!.visibility = View.VISIBLE
                }

                override fun onSuccess(objectFromJson: Any?) {
                    if (isDestroyed) return
                    progressBar!!.visibility = View.GONE
                    val userResult = objectFromJson as? UserResult
                    if (userResult != null && userResult.result != null) {
                        if (userResult.result!!.messageCode == 1) {
                            val user = userResult.result!!.user
                            if (user != null) {
                                application!!.sharedPreferences.edit().putInt(Constants.USER_ID, user.userID!!).apply()
                                application!!.sharedPreferences.edit().putInt(Constants.USER_TYPE, user.userType!!).apply()
                                application!!.sharedPreferences.edit().putBoolean(Constants.IS_LOGGED_IN, true).apply()
                                Toast.makeText(application, "Login successful", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(application, "Oops something went wrong", Toast.LENGTH_SHORT).show()
                            }
                        } else if (userResult.result!!.messageCode == 0) {
                            Toast.makeText(application, "Either mail id or password is wrong.", Toast.LENGTH_SHORT).show()
                        } else if (userResult.result!!.messageCode == -1) {
                            Toast.makeText(application, "User blocked by admin", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(application, "Oops something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onSuccess(isSuccess: Boolean) {
                    if (isDestroyed) return
                    progressBar!!.visibility = View.GONE
                }

                override fun onFailure() {
                    if (isDestroyed) return
                    progressBar!!.visibility = View.GONE
                    getLoginFailed()
                }

            })
            jsonServices.doLogin(edtUsername!!.text.toString(), edtPassword!!.text.toString())
        }

        btnSubmit!!.setOnClickListener { view ->
            if (Utils.isEmpty(edtName) || Utils.isEmpty(edtEmailId) || Utils.isEmpty(edtRegPassword)) {
                Toast.makeText(this@MainActivity, "Please verify entered values.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // For SignUp
            val jsonServices = JsonServiceOKHTTP(this@MainActivity, object : VolleyCallback {
                override fun starting() {
                    progressBar!!.visibility = View.VISIBLE
                }

                override fun onSuccess(objectFromJson: Any?) {
                    if (isDestroyed) return
                    progressBar!!.visibility = View.GONE
                    val registerResult = objectFromJson as? RegisterResult
                    if (registerResult != null && registerResult.response != null) {
                        val response = registerResult.response
                        if (response!!.isSuccessfullyRegistred) {
                            application!!.sharedPreferences.edit().putInt(Constants.USER_ID, response.isUserID).apply()
                            application!!.sharedPreferences.edit().putInt(Constants.USER_TYPE, 2).apply()
                            application!!.sharedPreferences.edit().putBoolean(Constants.IS_LOGGED_IN, true).apply()
                            Toast.makeText(application, response.message, Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(application, response.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(application, "Not able to register", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onSuccess(isSuccess: Boolean) {
                    if (isDestroyed) return
                    progressBar!!.visibility = View.GONE
                }

                override fun onFailure() {
                    if (isDestroyed) return
                    progressBar!!.visibility = View.GONE
                    getLoginFailed()
                }
            })
            jsonServices.registerUserAsync(edtName!!.text.toString(), edtEmailId!!.text.toString(),
                    edtRegPassword!!.text.toString(), Utils.currentDate)
        }
    }

    private fun getLoginFailed() {
        Toast.makeText(application, "UnFortunately Login Is Unsuccessful! .... Please Try Again Later ", Toast.LENGTH_SHORT).show()
        //onBackPressed();
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> application!!.permissionUtils.checkResults(grantResults, this)
        }
    }
}
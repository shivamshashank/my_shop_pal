package com.example.myshoppal.firestore

import androidx.appcompat.app.AppCompatActivity
import com.example.myshoppal.models.User
import com.example.myshoppal.ui.activities.LoginActivity
import com.example.myshoppal.ui.activities.RegisterActivity
import com.example.myshoppal.ui.activities.UserProfileActivity
import com.example.myshoppal.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, user: User) {
        mFirestore
            .collection("users")
            .document(user.id)
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                activity.registerUserSuccess(user)
            }
            .addOnFailureListener {
                activity.registerUserFailure()
            }
    }

    fun getUserDetails(activity: AppCompatActivity) {
        mFirestore.collection(Constants.USERS)
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener { document ->
                if (activity is LoginActivity) {
                    val user = document.toObject(User::class.java)!!
                    activity.getUserDetailsSuccess(user)
                }
            }
            .addOnFailureListener { e ->
                if (activity is LoginActivity) {
                    activity.getUserDetailsFailure(e)
                }
            }
    }

    fun updateUserDetails(activity: UserProfileActivity, userHashMap: HashMap<String, Any>) {
        mFirestore.collection(Constants.USERS)
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .update(userHashMap)
            .addOnSuccessListener { document ->
                activity.updateUserDetailsSuccess()
            }
            .addOnFailureListener { e ->
                activity.updateUserDetailsFailure()
            }
    }

}
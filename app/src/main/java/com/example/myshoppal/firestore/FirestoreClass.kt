package com.example.myshoppal.firestore

import android.app.Activity
import android.net.Uri
import android.util.Log
import com.example.myshoppal.models.User
import com.example.myshoppal.ui.activities.MainActivity
import com.example.myshoppal.ui.activities.RegisterActivity
import com.example.myshoppal.ui.activities.ProfileActivity
import com.example.myshoppal.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

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
            .addOnFailureListener { exception ->
                activity.registerUserFailure()

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    fun getUserDetails(activity: MainActivity) {
        mFirestore.collection(Constants.USERS)
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)!!
                activity.getUserDetailsSuccess(user)
            }
            .addOnFailureListener { exception ->
                activity.getUserDetailsFailure()

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    fun updateUserDetails(activity: ProfileActivity, userHashMap: HashMap<String, Any>) {
        mFirestore.collection(Constants.USERS)
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .update(userHashMap)
            .addOnSuccessListener {
                activity.updateUserDetailsSuccess()
            }
            .addOnFailureListener { exception ->
                activity.updateUserDetailsFailure()

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    fun uploadImage(activity: Activity, imageFileURI: Uri?, imageType: String) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity,
                imageFileURI,
            )
        )

        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())

                        when (activity) {
                            is ProfileActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                        }
                    }
            }
            .addOnFailureListener { exception ->
                when (activity) {
                    is ProfileActivity -> {
                        activity.imageUploadFailure()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

}
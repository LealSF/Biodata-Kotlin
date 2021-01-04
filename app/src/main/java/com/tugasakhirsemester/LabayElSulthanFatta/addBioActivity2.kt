package com.tugasakhirsemester.LabayElSulthanFatta

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_bio2.*
import java.util.jar.Manifest

class addBioActivity2 : AppCompatActivity() {

//    permission constanta
    private val CAMERA_REQUEST_CODE = 100
    private val STORAGE_REQUEST_CODE = 100

//    image pick constanta
    private val IMAGE_PICK_CAMERA_CODE = 102
    private val IMAGE_PICK_GALLERY_CODE = 103

//    array permission
    private lateinit var cameraPermission:Array<String>//camera dan storage
    private lateinit var storagePermission:Array<String>//hanya staroge

    private var imageUri: Uri? = null
    private var name:String? = ""
    private var alamat:String? = ""
    private var email:String? = ""
    private var telp:String? = ""
    private var lahir:String? = ""
    private var tempat:String? = ""
    private var kelamin:String? = ""

    private var actionBar: ActionBar? = null
    lateinit var dbHelper:MyDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bio2)

        actionBar = supportActionBar!!
        actionBar!!.title= "Add Biodata"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayShowHomeEnabled(true)

        dbHelper = MyDbHelper(this)

//        init permission array
        cameraPermission = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        storagePermission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        photoView.setOnClickListener {
            imagePickDialog()
        }
        savebtn.setOnClickListener {
            inputData()
        }
    }

    private fun inputData() {
        name = ""+NameT.text.toString().trim()
        alamat = ""+AlamatEt.text.toString().trim()
        email = ""+EmailEt.text.toString().trim()
        telp = ""+PhoneEt.text.toString().trim()
        lahir = ""+LahirEt.text.toString().trim()
        tempat = ""+TempatEt.text.toString().trim()
        kelamin = null

        val timestamp = System.currentTimeMillis()
        val id = dbHelper.insertRecord(
        ""+name,
        ""+imageUri,
        ""+alamat,
        ""+email,
        ""+telp,
        ""+lahir,
        ""+tempat,
        ""+kelamin
        )
        Toast.makeText(this, "Record Added against ID $id", Toast.LENGTH_SHORT).show()
    }


    private fun imagePickDialog() {
        val option = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ambil image")
        builder.setItems(option){dialog, which ->
            if (which==0){
                if (!checkCameraPermission()){
                    requestCameraPermission()
                }
                else{
                    pickFromCamera()
                }
            }
            else{
                if (!checkStoragePermission()){
                    requestStoragePermission()
                }
                else{
                    pickFromGallery()
                }
            }
        }
        builder.show()
    }

    private fun pickFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(
            galleryIntent,
            IMAGE_PICK_GALLERY_CODE
        )
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE)
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission( this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun pickFromCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Image Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image Description")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE)
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE)
    }

    private fun checkCameraPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this,
        android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        val resultsq = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        return result && resultsq
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_REQUEST_CODE ->{
                if(grantResults.isNotEmpty()){
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccepted && storageAccepted ) {
                        pickFromCamera()
                    }
                    else{
                        Toast.makeText(this, "Camera and storage permission are required", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            STORAGE_REQUEST_CODE ->{
                if(grantResults.isNotEmpty()){
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (storageAccepted){
                        pickFromGallery()
                    }
                    else{
                        Toast.makeText(this, "storage permission are required", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                CropImage.activity(data!!.data)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this)
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this)
            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK){
                    val resultUri = result.uri
                    imageUri = resultUri
                    photoView.setImageURI(resultUri)
                }
                else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    val error = result.error
                    Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
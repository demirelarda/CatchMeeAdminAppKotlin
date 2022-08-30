package com.mycompany.catchmeadminapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_add_item_to_store.*
import java.util.*
import kotlin.collections.ArrayList

private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
private lateinit var permissionLauncher: ActivityResultLauncher<String>
var selectedPicture: Uri? = null
private lateinit var auth : FirebaseAuth
private lateinit var storage : FirebaseStorage
private lateinit var db : FirebaseFirestore
val id : String = ""


class AddItemToStoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item_to_store)
        textView.visibility = View.INVISIBLE
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Add Items To Store"
        registerLauncher()
        auth = Firebase.auth
        storage = Firebase.storage
        db = Firebase.firestore


    }



    fun postProduct(view: View){

        postProductButton.visibility = View.INVISIBLE
        textView.visibility = View.VISIBLE

        //universal unique id
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val productId = uuid.toString()
        val productName = productNameEditText.text.toString()
        val productType = productTypeEditText.text.toString()
        val price = productPriceEditText.text.toString().toInt()

        val reference = storage.reference
        val imageReference = reference.child("productImages").child(imageName) //productImages/imageName

        if(selectedPicture != null){
            imageReference.putFile(selectedPicture!!).addOnSuccessListener {
                //download url -> firestore
                val uploadPictureReference = storage.reference.child("productImages").child(imageName)
                uploadPictureReference.downloadUrl.addOnSuccessListener {

                    val downloadUrl = it.toString()

                    val products = db.collection("products")
                    val values = hashMapOf(
                        "productId" to productId,
                        "productName" to productName,
                        "price" to price.toDouble(),
                        "productType" to productType,
                        "imageDownloadUrl" to downloadUrl
                    )

                    products.document(productId).set(values)
                    textView.visibility = View.INVISIBLE
                    Toast.makeText(this,"Product Uploaded Succesfully",Toast.LENGTH_LONG).show()
                    postProductButton.visibility = View.VISIBLE
                    productNameEditText.setText("")
                    productTypeEditText.setText("")
                    productPriceEditText.setText("")
                    imagePhotoImageView.setImageResource(R.drawable.selectimage)

                }

                }


        }


    }








    fun imageViewClicked(view: View){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission Needed For Gallery", Snackbar.LENGTH_INDEFINITE).setAction("Allow"){
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            }else{
                //request permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //start activity for result
            activityResultLauncher.launch(intentToGallery)

        }

    }


    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if(intentFromResult != null){
                    intentFromResult.data
                    selectedPicture = intentFromResult.data
                    selectedPicture.let {
                        imagePhotoImageView.setImageURI(it)
                    }
                }
            }

        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if(result){
                //permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                //permission denied
                Toast.makeText(this,"Permission Needed For Gallery", Toast.LENGTH_LONG).show()
            }
        }
    }


}
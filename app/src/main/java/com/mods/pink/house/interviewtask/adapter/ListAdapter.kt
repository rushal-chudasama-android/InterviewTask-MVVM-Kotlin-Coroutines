package com.mods.pink.house.interviewtask.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mods.pink.house.interviewtask.R
import com.mods.pink.house.interviewtask.model.ApiResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ListAdapter(private var list: List<ApiResponseItem>,private val applicationContext: Context) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(newList: List<ApiResponseItem>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.imageView.setImageBitmap(null) // Clear previous image
        GlobalScope.launch {

            loadImage(list[position].thumbnail.domain + "/" + list[position].thumbnail.basePath + "/" + list[position].thumbnail.qualities[1] + "/" + list[position].thumbnail.key) { bitmap ->
                if (bitmap != null) {
                    holder.imageView.setImageBitmap(bitmap)
                } else {
                    // Handle image loading failure
                    holder.imageView.setImageResource(R.drawable.ic_launcher_foreground)
                }
            }
        }

        /*   Glide.with(holder.textViewTitle.context)
               .load(list[position].thumbnail.domain+"/" + list[position].thumbnail.basePath + "/" + list[position].thumbnail.qualities[1] + "/" + list[position].thumbnail.key)
               .into(holder.textViewTitle)*/
    }


    private suspend fun loadImage(imageUrl: String, callback: (Bitmap?) -> Unit) {
        withContext(Dispatchers.IO) {
            try {
                val cacheDir = File(applicationContext.cacheDir, "imageCache")
                if (!cacheDir.exists()) cacheDir.mkdirs()

                val fileName = imageUrl.hashCode().toString()
                val cachedFile = File(cacheDir, fileName)
                val bitmap: Bitmap?

                if (cachedFile.exists()) {
                    val inputStream = FileInputStream(cachedFile)
                    bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream.close()
                } else {
                    val url = URL(imageUrl)
                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                    connection.doInput = true
                    connection.connect()
                    val inputStream: InputStream = connection.inputStream
                    bitmap = BitmapFactory.decodeStream(inputStream)

                    val outputStream = FileOutputStream(cachedFile)
                    bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.flush()
                    outputStream.close()
                    inputStream.close()
                }

                callback(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
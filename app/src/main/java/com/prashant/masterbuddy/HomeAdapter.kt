package com.prashant.masterbuddy

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.prashant.masterbuddy.utils.Utils
import com.prashant.masterbuddy.ws.model.File
import kotlinx.android.synthetic.main.lyt_home_list_item.view.*

class HomeAdapter(private val context: Context, val channelType: Int, val mediaType: Int,
                  private val files: ArrayList<File>, private val filesCount: Int): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    var isCheckingForNewFiles = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.lyt_home_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return files.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtTitle.text = files[position].title
        Glide.with(context)
                .load(files[position].thumbnailUrl)
                .apply(RequestOptions().placeholder(R.drawable.thumbnail_default))
                .into(holder.imgThumbnail)
        holder.ibOption.setOnClickListener { v -> download(v, position) }
        holder.itemView.setOnClickListener {
            when (mediaType) {
                Constants.MEDIA_VIDEO, Constants.MEDIA_AUDIO -> {
                    val intent = Intent(context, PlayVideoActivity::class.java)
                    intent.putExtra("URL", files[position].fileUrl)
                    intent.putExtra("CHANNEL", channelType)
                    context.startActivity(intent)
                }
                Constants.MEDIA_IMAGE -> {
                    val intent = Intent(context, ImageViewActivity::class.java)
                    intent.putExtra("URL", files[position].fileUrl)
                    context.startActivity(intent)
                }
                Constants.MEDIA_DOCS -> if (channelType == Constants.CHANNEL_SAVED) {
                    Utils.openChooserForFile(context, java.io.File(files[position].fileUrl!!))
                } else {
                    DocumentDownloader.openDocumentOrDownload(context, files[position])
                }
            }
        }

        if (channelType != Constants.CHANNEL_SAVED && !isCheckingForNewFiles
                && filesCount > files.size && position >= files.size-10) {
            isCheckingForNewFiles = true
            (context as HomeActivity).checkForNewFiles(channelType, mediaType, files.size)
        }
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imgThumbnail = view.imgThumbnail!!
        val txtTitle = view.txtTitle!!
        val ibOption = view.ibOption!!
    }

    fun download(view: View, position: Int) {
        val popup = PopupMenu(context, view)
        popup.menuInflater.inflate(R.menu.menu_popup, popup.menu)
        if (channelType == Constants.CHANNEL_SAVED) popup.menu.findItem(R.id.menuDownload).title = "Remove"
        popup.setOnMenuItemClickListener { item ->
            if (channelType == Constants.CHANNEL_SAVED) {
                (context.applicationContext as Application).savedMediaDataSource.deleteSavedFile(files[position].id!!)
                files.removeAt(position)
                notifyItemRemoved(position)
                if (files[position].fileUrl != null) {
                    java.io.File(files[position].fileUrl!!).delete()
                }
                if (files[position].thumbnailUrl != null) {
                    java.io.File(files[position].thumbnailUrl!!).delete()
                }
                Toast.makeText(context, "Removed Successfully", Toast.LENGTH_SHORT).show()
            } else {
                DownloaderService.startDownload(context, files[position], channelType, mediaType)
            }
            false
        }
        popup.show()
    }
}
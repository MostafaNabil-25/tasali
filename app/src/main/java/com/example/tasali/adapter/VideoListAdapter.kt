package com.example.tasali.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tasali.ProfileActivity
import com.example.tasali.R
import com.example.tasali.databinding.VideoItemRowBinding
import com.example.tasali.model.UserModel
import com.example.tasali.model.VideoModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.toObject

class VideoListAdapter(
    options: FirestoreRecyclerOptions<VideoModel>
) : FirestoreRecyclerAdapter<VideoModel,VideoListAdapter.VideoViewHolder>(options) {

    inner class VideoViewHolder(private val binding:VideoItemRowBinding):RecyclerView.ViewHolder(binding.root){
        fun bindVideo(videoModel:VideoModel){

            Firebase.firestore.collection("users")
                .document(videoModel.uploaderId)
                .get().addOnSuccessListener {
                   val userModel = it?.toObject(UserModel::class.java)
                    userModel?.apply {
                        binding.usernameView.text = username
                        Glide.with(binding.profileIcon).load(profilePic)
                            .circleCrop()
                            .apply(RequestOptions().placeholder(R.drawable.icon_account_circle))
                            .into(binding.profileIcon)

                        binding.userDetailLayout.setOnClickListener {
                            val intent = Intent(binding.userDetailLayout.context, ProfileActivity::class.java)
                            intent.putExtra("profile_user_id",id)
                            binding.userDetailLayout.context.startActivity(intent)

                        }
                    }

                }
            binding.captionView.text = videoModel.title
            binding.progressBar.visibility = View.VISIBLE



            binding.videoView.apply {
                setVideoPath(videoModel.url)
                setOnPreparedListener {
                    binding.progressBar.visibility = View.GONE
                    it.start()
                    it.isLooping = true

                }
                setOnClickListener {
                    if (isPlaying){
                        pause()
                        binding.pauseIcon.visibility = View.VISIBLE
                    }else{
                        start()
                        binding.pauseIcon.visibility = View.GONE
                    }
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = VideoItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(p0: VideoViewHolder, p1: Int, p2: VideoModel) {
        p0.bindVideo(p2)
    }
}
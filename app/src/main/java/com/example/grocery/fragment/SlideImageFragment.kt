package com.example.grocery.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.grocery.R
import com.example.grocery.manager.Config
import com.example.grocery.module.Image
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_slide_image.view.*


class SlideImageFragment : Fragment() {
    private var imgId:String?=""
    private var imgSrc:String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imgId=it.getString(Image.TITLE)
            imgSrc=it.getString(Image.SRC)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view= inflater.inflate(R.layout.fragment_slide_image, container, false)
        init(view)
        return view
    }

    private fun init(view:View){
        Picasso.get()
        .load(imgSrc)
        .placeholder(R.drawable.no_img)
        .resize(1000,700)
        .centerInside()
        .into(view.image_view)
    }


    companion object {
        @JvmStatic
        fun newInstance(imgTitle: String,imgSrc:String) =
            SlideImageFragment().apply {
                arguments = Bundle().apply {
                    putString(Image.TITLE, imgTitle)
                    putString(Image.SRC, imgSrc)
                }
            }
    }


}
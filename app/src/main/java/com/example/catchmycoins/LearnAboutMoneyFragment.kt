package com.example.catchmycoins

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.core.text.HtmlCompat
import android.content.Intent
import android.net.Uri

class LearnAboutMoneyFragment : Fragment() {

    private lateinit var backButton: ImageView
    private lateinit var blogLinkTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_learn_about_money, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton = view.findViewById(R.id.back)
        blogLinkTextView = view.findViewById(R.id.textView13) // Assuming the "Want more tips?" TextView has this ID

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Make the blog link clickable
        val linkText = "Want more tips? Check out our blog!<font color='#FFEB3B'>&#x1F517;</font>" // Added link icon
        blogLinkTextView.text = HtmlCompat.fromHtml(linkText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        blogLinkTextView.setOnClickListener {
            openBlogLink()
        }
    }

    private fun openBlogLink() {
        val blogUrl = "https://kgosi-sebako-d23a3.web.app/" // Replace with your actual blog URL
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(blogUrl))
        startActivity(intent)
    }
}
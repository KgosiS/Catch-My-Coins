package com.example.catchmycoins

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

class BadgesEarnedFragment : Fragment() {

    // Declare LinearLayout variables to hold references to the income and expense badge cards
    private lateinit var incomeBadgeCard: LinearLayout
    private lateinit var expenseBadgeCard: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment and return the inflated view
        return inflater.inflate(R.layout.fragment_badges_earned, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the LinearLayout variables by finding their corresponding views in the inflated layout
        incomeBadgeCard = view.findViewById(R.id.incomeBadgeCard)
        expenseBadgeCard = view.findViewById(R.id.expenseBadgeCard)

        // Set an OnClickListener for the income badge card
        incomeBadgeCard.setOnClickListener {
            // When clicked, open the IncomeBadgesFragment
            openFragment(IncomeBadgesFragment())
        }

        // Set an OnClickListener for the expense badge card
        expenseBadgeCard.setOnClickListener {
            // When clicked, open the ExpenseBadgesFragment
            openFragment(ExpenseBadgesFragment())
        }
    }

    /**
     * Helper function to replace the current fragment with a new one.
     * @param fragment The new Fragment to display.
     */
    private fun openFragment(fragment: Fragment) {
        // Begin a FragmentTransaction
        val transaction = parentFragmentManager.beginTransaction()
        // Replace the content of the 'fragment_container' (assuming this is your host container ID)
        // with the new fragment
        transaction.replace(R.id.fragment_container, fragment)
        // Add the transaction to the back stack, allowing the user to navigate back
        transaction.addToBackStack(null)
        // Commit the transaction to apply the changes
        transaction.commit()
    }
}
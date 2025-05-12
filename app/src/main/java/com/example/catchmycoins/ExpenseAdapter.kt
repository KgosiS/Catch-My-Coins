package com.example.catchmycoins



import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.net.Uri

class ExpenseAdapter(private var transactions: List<Expense>) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val amountText: TextView = itemView.findViewById(R.id.amountText)
        val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val imageView: ImageView = itemView.findViewById(R.id.transactionImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.amountText.text = "R${transaction.amount}"
        holder.descriptionText.text = transaction.description
        holder.dateText.text = transaction.date

        // Handle image if present
        if (!transaction.imagePath.isNullOrEmpty()) {
            holder.imageView.setImageURI(Uri.parse(transaction.imagePath))
            holder.imageView.visibility = View.VISIBLE

            // Click listener to show the image in a dialog
            holder.imageView.setOnClickListener {
                showImageDialog(transaction.imagePath, holder.itemView.context) // Use itemView.context
            }
        } else {
            holder.imageView.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = transactions.size

    fun updateData(newTransactions: List<Expense>) {
        transactions = newTransactions
        notifyDataSetChanged() // Refresh the RecyclerView
    }

    // Show the image in a Dialog
    private fun showImageDialog(imagePath: String?, context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_image_view)

        val imageView: ImageView = dialog.findViewById(R.id.dialogImageView)
        val closeButton: ImageView = dialog.findViewById(R.id.closeButton)

        // Load the image from the URI
        if (!imagePath.isNullOrEmpty()) {
            imageView.setImageURI(Uri.parse(imagePath))
        }

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}

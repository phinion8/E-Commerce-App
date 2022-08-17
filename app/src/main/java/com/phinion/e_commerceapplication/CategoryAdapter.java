package com.phinion.e_commerceapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phinion.e_commerceapplication.databinding.ItemCategoryBinding;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewModel> {

    Context context;
    ArrayList<Category> categories;

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewModel(LayoutInflater.from(context).inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewModel holder, int position) {


        Category category = categories.get(position);
        holder.binding.label.setText(Html.fromHtml(category.getName()));

        Glide.with(context)
                .load(category.getIcon())
                .into(holder.binding.image);


        holder.binding.image.setBackgroundColor(Color.parseColor(category.getColor()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("catId", category.getId());
                intent.putExtra("categoryName", category.getName());
                context.startActivity(intent);
            }
        });




    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewModel extends RecyclerView.ViewHolder {

        ItemCategoryBinding binding;

        public CategoryViewModel(@NonNull View itemView) {
            super(itemView);
            binding = ItemCategoryBinding.bind(itemView);
        }
    }
}

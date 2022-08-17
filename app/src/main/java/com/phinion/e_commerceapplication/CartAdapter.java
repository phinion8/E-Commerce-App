package com.phinion.e_commerceapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;
import com.phinion.e_commerceapplication.databinding.ItemCartBinding;
import com.phinion.e_commerceapplication.databinding.QuantityDialogBinding;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    Context context;
    ArrayList<Product> products;
    CartListener cartListener;
    Cart cart;


    public CartAdapter(Context context, ArrayList<Product> products, CartListener cartListener){
        this.context = context;
        this.products = products;
        this.cartListener = cartListener;
        cart = TinyCartHelper.getCart();;
    }


    public interface CartListener{
        public void OnQuantityChanged();
    }




    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = products.get(position);



        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.image);

        holder.binding.price.setText("INR " + product.getPrice());
        holder.binding.name.setText(product.getName());
        holder.binding.quantity.setText(product.getQuantity() + " item(s)");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                QuantityDialogBinding quantityDialogBinding = QuantityDialogBinding.inflate(LayoutInflater.from(context));
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setView(quantityDialogBinding.getRoot())
                        .create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
                dialog.show();

                quantityDialogBinding.name.setText(product.getName());
                quantityDialogBinding.productStock.setText("Stock: " + product.getStock());
                quantityDialogBinding.quantity.setText(String.valueOf(product.getQuantity()));


                quantityDialogBinding.plusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int quantity = product.getQuantity();
                        quantity++;

                        if (quantity > product.getStock()){
                            Toast.makeText(context, "Max stock available", Toast.LENGTH_SHORT).show();
                        }else{
                            product.setQuantity(quantity);
                            quantityDialogBinding.quantity.setText(String.valueOf(quantity));
                        }


                    }
                });

                quantityDialogBinding.minusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int quantity = product.getQuantity();


                        if (quantity > 1){
                            quantity--;
                        }

                        product.setQuantity(quantity);
                        quantityDialogBinding.quantity.setText(String.valueOf(quantity));

                    }
                });

                quantityDialogBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        notifyDataSetChanged();

                        cart.updateItem(product, product.getQuantity());
                        cartListener.OnQuantityChanged();


                    }
                });

            }
        });



    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        ItemCartBinding binding;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCartBinding.bind(itemView);
        }
    }
}

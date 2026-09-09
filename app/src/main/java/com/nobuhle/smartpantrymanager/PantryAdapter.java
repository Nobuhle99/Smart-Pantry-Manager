package com.nobuhle.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private final List<PantryItem> pantryItems;

    public PantryAdapter(List<PantryItem> pantryItems) {
        this.pantryItems = pantryItems;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);

        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PantryViewHolder holder,
            int position) {

        PantryItem item = pantryItems.get(position);

        holder.tvIngredientName.setText(item.getName());

        String quantityText =
                "Quantity: " + item.getQuantity() + " " + item.getUnit();

        holder.tvIngredientQuantity.setText(quantityText);

        // EDIT button
        holder.btnEditIngredient.setOnClickListener(v -> {

            Context context = v.getContext();

            Intent intent = new Intent(
                    context,
                    AddEditIngredientActivity.class
            );

            // Send the existing ingredient information
            intent.putExtra("ingredient_id", item.getId());
            intent.putExtra("ingredient_name", item.getName());
            intent.putExtra("ingredient_quantity", item.getQuantity());
            intent.putExtra("ingredient_unit", item.getUnit());

            context.startActivity(intent);
        });

        // DELETE button
        holder.btnDeleteIngredient.setOnClickListener(v -> {

            Context context = v.getContext();

            SmartPantryDatabase database =
                    new SmartPantryDatabase(context);

            int result =
                    database.deletePantryItem(item.getId());

            if (result > 0) {

                // Remove from the displayed list
                int currentPosition = holder.getAdapterPosition();

                if (currentPosition != RecyclerView.NO_POSITION) {
                    pantryItems.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                }

                Toast.makeText(
                        context,
                        "Ingredient deleted successfully!",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                Toast.makeText(
                        context,
                        "Failed to delete ingredient.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    public static class PantryViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvIngredientName;
        TextView tvIngredientQuantity;

        Button btnEditIngredient;
        Button btnDeleteIngredient;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvIngredientName =
                    itemView.findViewById(R.id.tvIngredientName);

            tvIngredientQuantity =
                    itemView.findViewById(R.id.tvIngredientQuantity);

            btnEditIngredient =
                    itemView.findViewById(R.id.btnEditIngredient);

            btnDeleteIngredient =
                    itemView.findViewById(R.id.btnDeleteIngredient);
        }
    }
}
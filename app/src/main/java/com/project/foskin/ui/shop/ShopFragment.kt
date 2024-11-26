package com.project.foskin.ui.shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.foskin.R

class ShopFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)

        val rvMenuSkincare = view.findViewById<RecyclerView>(R.id.rvCarouselSkincare)
        rvMenuSkincare.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter1 = MenuSkincareAdapter(getMenuData()) { menuItem ->
            Toast.makeText(context, "Clicked: ${menuItem.name}", Toast.LENGTH_SHORT).show()
        }
        rvMenuSkincare.adapter = adapter1

        val rvRecommended = view.findViewById<RecyclerView>(R.id.rvRecommended)
        rvRecommended.layoutManager = GridLayoutManager(context, 2) // 2 kolom
        val adapter2 = ProductRecommendedAdapter(requireContext(), getProductData())
        rvRecommended.adapter = adapter2

        return view
    }

    private fun getMenuData(): List<MenuItemSkincare> {
        return listOf(
            MenuItemSkincare(
                name = "Facial Wash",
                imageResId = "https://drive.google.com/uc?id=1yIUswx4ji56HoodLKII3D1zGkEfamZNG"
            ),
            MenuItemSkincare(
                name = "Toner",
                imageResId = "https://drive.google.com/uc?id=1EYSH6AlQZAohKn9pn7sqf2oZ_p_UMUg8"
            ),
            MenuItemSkincare(
                name = "Micellar Water",
                imageResId = "https://drive.google.com/uc?id=1vu1M5Zno-4av8uEWBv6EiwRr_X1tA_up"
            ),
            MenuItemSkincare(
                name = "Essence",
                imageResId = "https://drive.google.com/uc?id=19vDHsAvEPMeUgEOoGlm4CgbZ45ucMh6u"
            ),
            MenuItemSkincare(
                name = "Serum",
                imageResId = "https://drive.google.com/uc?id=1uvulYH7IwBPp1h4dZiypksteEJXtiA_y"
            ),
            MenuItemSkincare(
                name = "Sheet Mask",
                imageResId = "https://drive.google.com/uc?id=1gZiUsVV7twTNFt9PZhvcb4Hh-pnLzE5B"
            ),
            MenuItemSkincare(
                name = "Ampoule",
                imageResId = "https://drive.google.com/uc?id=1gJ2H_KznV7T8STOqcy8xtoVXTqPZe34u"
            ),
            MenuItemSkincare(
                name = "Moisturizer",
                imageResId = "https://drive.google.com/uc?id=1Qo1wMvbWi7JIw_15aenyacVqfetFpjd2"
            ),
            MenuItemSkincare(
                name = "Eye Cream",
                imageResId = "https://drive.google.com/uc?id=1wNX4fKpsYUc6Vg3e4VVs-XMNs4tYZMR9"
            ),
            MenuItemSkincare(
                name = "Exfoliator",
                imageResId = "https://drive.google.com/uc?id=1_bp5uqsBdiwvKUPXOtsmZW98rIcqTcKg"
            ),
            MenuItemSkincare(
                name = "Sunscreen",
                imageResId = "https://drive.google.com/uc?id=1_F42W8R9Ct6lWHQ4525HToi-ZHu8CXSn"
            ),
            MenuItemSkincare(
                name = "Face Oil",
                imageResId = "https://drive.google.com/uc?id=1oJZoB4pHQ9vnBR98DU5idJd104RtyJpQ"
            ),
            MenuItemSkincare(
                name = "Spot Treatment",
                imageResId = "https://drive.google.com/uc?id=1UuzXoUZ9-35GRHuYIGCv1iG6pQ1ZDX5-"
            ),
            MenuItemSkincare(
                name = "Sleeping Mask",
                imageResId = "https://drive.google.com/uc?id=1XYl4WCL1k6SyfSvuk0ZUW9jyDRwFwbpK"
            ),
            MenuItemSkincare(
                name = "Face Mist",
                imageResId = "https://drive.google.com/uc?id=1ql1It9etDxLJJ0w7i1p72BoOtUU9_ELU"
            )
        )
    }

    private fun getProductData(): List<ProductItem> {
        return listOf(
            ProductItem(
                imageUrl = "https://drive.google.com/uc?id=1yIUswx4ji56HoodLKII3D1zGkEfamZNG",
                title = "Foskin Low pH Good Morning Celaser - 250 Ml",
                price = 50000,
                rating = 4.5f,
                soldCount = 120,
                shopLink = "https://shopee.co.id/product-link-1"
            ),
            ProductItem(
                imageUrl = "https://drive.google.com/uc?id=1EYSH6AlQZAohKn9pn7sqf2oZ_p_UMUg8",
                title = "Toner",
                price = 60000,
                rating = 4.0f,
                soldCount = 100,
                shopLink = "https://shopee.co.id/product-link-2"
            ),
            ProductItem(
                imageUrl = "https://drive.google.com/uc?id=1vu1M5Zno-4av8uEWBv6EiwRr_X1tA_up",
                title = "Micellar Water",
                price = 75000,
                rating = 4.2f,
                soldCount = 150,
                shopLink = "https://shopee.co.id/product-link-3"
            ),
            ProductItem(
                imageUrl = "https://drive.google.com/uc?id=19vDHsAvEPMeUgEOoGlm4CgbZ45ucMh6u",
                title = "Essence",
                price = 80000,
                rating = 4.8f,
                soldCount = 200,
                shopLink = "https://shopee.co.id/product-link-4"
            ),
            ProductItem(
                imageUrl = "https://drive.google.com/uc?id=1uvulYH7IwBPp1h4dZiypksteEJXtiA_y",
                title = "Serum",
                price = 65000,
                rating = 4.4f,
                soldCount = 130,
                shopLink = "https://shopee.co.id/product-link-5"
            ),
            ProductItem(
                imageUrl = "https://drive.google.com/uc?id=1gZiUsVV7twTNFt9PZhvcb4Hh-pnLzE5B",
                title = "Sheet Mask",
                price = 40000,
                rating = 4.1f,
                soldCount = 110,
                shopLink = "https://shopee.co.id/product-link-6"
            ),
            ProductItem(
                imageUrl = "https://drive.google.com/uc?id=1gJ2H_KznV7T8STOqcy8xtoVXTqPZe34u",
                title = "Ampoule",
                price = 45000,
                rating = 4.3f,
                soldCount = 140,
                shopLink = "https://shopee.co.id/product-link-7"
            ),
            ProductItem(
                imageUrl = "https://drive.google.com/uc?id=1Qo1wMvbWi7JIw_15aenyacVqfetFpjd2",
                title = "Moisturizer",
                price = 25000,
                rating = 4.7f,
                soldCount = 90,
                shopLink = "https://shopee.co.id/product-link-8"
            ),
            ProductItem(
                imageUrl = "https://drive.google.com/uc?id=1wNX4fKpsYUc6Vg3e4VVs-XMNs4tYZMR9",
                title = "Eye Cream",
                price = 120000,
                rating = 4.9f,
                soldCount = 180,
                shopLink = "https://shopee.co.id/product-link-9"
            ),
            ProductItem(
                imageUrl = "https://drive.google.com/uc?id=1_bp5uqsBdiwvKUPXOtsmZW98rIcqTcKg",
                title = "Exfoliator",
                price = 30000,
                rating = 4.6f,
                soldCount = 160,
                shopLink = "https://shopee.co.id/product-link-10"
            ),
            ProductItem(
                imageUrl = "https://drive.google.com/uc?id=1_F42W8R9Ct6lWHQ4525HToi-ZHu8CXSn",
                title = "Sunscreen",
                price = 40000,
                rating = 4.5f,
                soldCount = 140,
                shopLink = "https://shopee.co.id/product-link-11"
            ),
            ProductItem(
                imageUrl = "https://drive.google.com/uc?id=1oJZoB4pHQ9vnBR98DU5idJd104RtyJpQ",
                title = "Face Oil",
                price = 95000,
                rating = 4.3f,
                soldCount = 180,
                shopLink = "https://shopee.co.id/product-link-12"
            ),
            ProductItem(
                imageUrl = "https://drive.google.com/uc?id=1UuzXoUZ9-35GRHuYIGCv1iG6pQ1ZDX5-",
                title = "Spot Treatment",
                price = 155000,
                rating = 4.2f,
                soldCount = 125,
                shopLink = "https://shopee.co.id/product-link-13"
            ),
            ProductItem(
                imageUrl = "https://drive.google.com/uc?id=1XYl4WCL1k6SyfSvuk0ZUW9jyDRwFwbpK",
                title = "Sleeping Mask",
                price = 5000,
                rating = 4.4f,
                soldCount = 210,
                shopLink = "https://shopee.co.id/product-link-14"
            ),
            ProductItem(
                imageUrl = "https://drive.google.com/uc?id=1ql1It9etDxLJJ0w7i1p72BoOtUU9_ELU",
                title = "Face Mist",
                price = 125000,
                rating = 4.6f,
                soldCount = 175,
                shopLink = "https://shopee.co.id/product-link-15"
            )
        )
    }
}

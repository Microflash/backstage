package com.example.jackson.polymorphic;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "itemCategory")
sealed interface CartItem permits Software, Accessory {

	ItemCategory itemCategory();
}

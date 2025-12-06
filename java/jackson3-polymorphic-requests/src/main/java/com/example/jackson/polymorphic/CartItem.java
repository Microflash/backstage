package com.example.jackson.polymorphic;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "itemCategory")
@JsonSubTypes({
		@JsonSubTypes.Type(value = Software.class, name = "SOFTWARE"),
		@JsonSubTypes.Type(value = Accessory.class, name = "ACCESSORY"),
})
sealed interface CartItem permits Software, Accessory {

	ItemCategory itemCategory();
}

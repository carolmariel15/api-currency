package com.nttdata.api.currency.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Unwrapped.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "currency")
public class Currency {

    @Id
    private Integer id;
    private String description;
    @Nullable
    private Double buyingRate;
    @Nullable
    private Double sellingRate;
}

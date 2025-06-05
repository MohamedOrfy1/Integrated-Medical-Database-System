package com.example.demo.DTO;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BloodTestAttributeDTO {
    private String attributeName;
    private Double attributeValue;
    private String unit;
    private Double fromRange;
    private Double toRange;

    public BloodTestAttributeDTO(String attributeName, Double attributeValue,
                                        String unit, Double fromRange, Double toRange) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.unit = unit;
        this.fromRange = fromRange;
        this.toRange = toRange;
    }


    public boolean isWithinRange() {
        if (attributeValue == null || fromRange == null || toRange == null) {
            return false;
        }
        return attributeValue >= fromRange && attributeValue <= toRange;
    }

    @Override
    public String toString() {
        return String.format("%s: %.2f %s (Reference Range: %.2f - %.2f %s) %s",
                attributeName,
                attributeValue,
                unit,
                fromRange,
                toRange,
                unit,
                isWithinRange() ? "[Normal]" : "[Abnormal]");
    }
}

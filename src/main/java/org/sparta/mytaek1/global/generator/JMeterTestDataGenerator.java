package org.sparta.mytaek1.global.generator;

import org.sparta.mytaek1.domain.broadcast.dto.BroadcastRequestDto;
import org.sparta.mytaek1.domain.order.dto.OrderRequestDto;
import org.sparta.mytaek1.domain.user.dto.UserRequestDto;

import java.util.ArrayList;
import java.util.List;

public class JMeterTestDataGenerator {

    public static void main(String[] args) {
        List<OrderRequestDto> testDataList = generateTestData(500);

        for (int i = 0; i < testDataList.size(); i++) {
            OrderRequestDto testData = testDataList.get(i);
            System.out.printf("%d,%d%n",
                    testData.getQuantity(), testData.getTotalPrice());
        }
    }

    private static List<OrderRequestDto> generateTestData(int count) {
        List<OrderRequestDto> testDataList = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            int quantity = 1;
            int totalPrice = 1000;

            OrderRequestDto testData = new OrderRequestDto(quantity, totalPrice);
            testDataList.add(testData);
        }

        return testDataList;
    }
}
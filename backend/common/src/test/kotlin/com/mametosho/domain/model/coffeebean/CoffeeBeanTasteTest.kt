package com.mametosho.domain.model.coffeebean

import com.mametosho.domain.model.taste.TasteId
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class CoffeeBeanTasteTest {

    @Test
    fun `evaluationValueが0の場合は生成できる`() {
        val taste = CoffeeBeanTaste(
            id = CoffeeBeanTasteId("00000000-0000-4000-8000-000000000008"),
            tasteId = TasteId("00000000-0000-4000-8000-000000000004"),
            evaluationValue = 0,
        )
        assertEquals(0, taste.evaluationValue)
    }

    @Test
    fun `evaluationValueが正の値の場合は生成できる`() {
        val taste = CoffeeBeanTaste(
            id = CoffeeBeanTasteId("00000000-0000-4000-8000-000000000008"),
            tasteId = TasteId("00000000-0000-4000-8000-000000000004"),
            evaluationValue = 5,
        )
        assertEquals(5, taste.evaluationValue)
    }

    @Test
    fun `evaluationValueが負の値の場合は例外が発生する`() {
        assertThrows<IllegalArgumentException> {
            CoffeeBeanTaste(
                id = CoffeeBeanTasteId("00000000-0000-4000-8000-000000000008"),
                tasteId = TasteId("00000000-0000-4000-8000-000000000004"),
                evaluationValue = -1,
            )
        }
    }
}

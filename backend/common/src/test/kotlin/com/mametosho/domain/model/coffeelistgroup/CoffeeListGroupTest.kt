package com.mametosho.domain.model.coffeelistgroup

import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class CoffeeListGroupTest {

    private fun createCoffeeListGroup(
        children: List<CoffeeListChild> = emptyList(),
    ): CoffeeListGroup = CoffeeListGroup(
        id = CoffeeListGroupId("00000000-0000-4000-8000-00000000000f"),
        description = "2026年2月のおすすめ珈琲豆",
        children = children,
    )

    @Test
    fun `正常にCoffeeListGroupを生成できる`() {
        val group = createCoffeeListGroup()
        assertEquals("2026年2月のおすすめ珈琲豆", group.description)
        assertEquals(0, group.children.size)
    }

    @Test
    fun `descriptionがnullでも生成できる`() {
        val group = createCoffeeListGroup().copy(description = null)
        assertEquals(null, group.description)
    }

    @Test
    fun `異なるCoffeeBeanIdの明細を複数持てる`() {
        val children = listOf(
            CoffeeListChild(
                id = CoffeeListChildId("00000000-0000-4000-8000-000000000010"),
                coffeeBeanId = CoffeeBeanId("00000000-0000-4000-8000-000000000001"),
            ),
            CoffeeListChild(
                id = CoffeeListChildId("00000000-0000-4000-8000-000000000011"),
                coffeeBeanId = CoffeeBeanId("00000000-0000-4000-8000-000000000002"),
            ),
        )
        val group = createCoffeeListGroup(children = children)
        assertEquals(2, group.children.size)
    }

    @Test
    fun `同じCoffeeBeanIdの明細が重複する場合は例外が発生する`() {
        val duplicateBeanId = CoffeeBeanId("00000000-0000-4000-8000-000000000001")
        val children = listOf(
            CoffeeListChild(
                id = CoffeeListChildId("00000000-0000-4000-8000-000000000010"),
                coffeeBeanId = duplicateBeanId,
            ),
            CoffeeListChild(
                id = CoffeeListChildId("00000000-0000-4000-8000-000000000011"),
                coffeeBeanId = duplicateBeanId,
            ),
        )
        assertThrows<IllegalArgumentException> {
            createCoffeeListGroup(children = children)
        }
    }

    @Test
    fun `addChildで珈琲豆を追加できる`() {
        val group = createCoffeeListGroup()
        val updated = group.addChild(
            id = CoffeeListChildId("00000000-0000-4000-8000-000000000010"),
            coffeeBeanId = CoffeeBeanId("00000000-0000-4000-8000-000000000001"),
        )
        assertEquals(1, updated.children.size)
        assertEquals(CoffeeBeanId("00000000-0000-4000-8000-000000000001"), updated.children[0].coffeeBeanId)
    }

    @Test
    fun `addChildで既に存在する珈琲豆を追加すると例外が発生する`() {
        val beanId = CoffeeBeanId("00000000-0000-4000-8000-000000000001")
        val group = createCoffeeListGroup(
            children = listOf(
                CoffeeListChild(
                    id = CoffeeListChildId("00000000-0000-4000-8000-000000000010"),
                    coffeeBeanId = beanId,
                ),
            ),
        )
        assertThrows<IllegalArgumentException> {
            group.addChild(
                id = CoffeeListChildId("00000000-0000-4000-8000-000000000011"),
                coffeeBeanId = beanId,
            )
        }
    }

    @Test
    fun `removeChildで珈琲豆を削除できる`() {
        val beanId = CoffeeBeanId("00000000-0000-4000-8000-000000000001")
        val group = createCoffeeListGroup(
            children = listOf(
                CoffeeListChild(
                    id = CoffeeListChildId("00000000-0000-4000-8000-000000000010"),
                    coffeeBeanId = beanId,
                ),
            ),
        )
        val updated = group.removeChild(beanId)
        assertEquals(0, updated.children.size)
    }

    @Test
    fun `removeChildで存在しない珈琲豆を削除すると例外が発生する`() {
        val group = createCoffeeListGroup()
        assertThrows<IllegalArgumentException> {
            group.removeChild(CoffeeBeanId("00000000-0000-4000-8000-000000000001"))
        }
    }
}

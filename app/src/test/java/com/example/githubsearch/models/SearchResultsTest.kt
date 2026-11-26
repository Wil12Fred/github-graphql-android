package com.example.githubsearch.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchResultsTest {

    @Test
    fun repoFieldsLandInTheRightSlots() {
        // Regression: the screen used to pass owner and description in swapped order.
        val repo = SearchResults.toRepo(3, "MDEw", "hello", "octocat", "My first repo")!!
        assertEquals(3, repo.id)
        assertEquals("MDEw", repo.stringId)
        assertEquals("hello", repo.name)
        assertEquals("octocat", repo.owner)
        assertEquals("My first repo", repo.descripcion)
    }

    @Test
    fun repoWithoutDescriptionIsKeptWithEmptyText() {
        // Regression: description()!! crashed on repositories with no description.
        val repo = SearchResults.toRepo(0, "id", "name", "owner", null)!!
        assertEquals("", repo.descripcion)
    }

    @Test
    fun repoMissingRequiredFieldsIsDropped() {
        assertNull(SearchResults.toRepo(0, null, "n", "o", "d"))
        assertNull(SearchResults.toRepo(0, "id", null, "o", "d"))
        assertNull(SearchResults.toRepo(0, "id", "n", null, "d"))
    }

    @Test
    fun userWithoutDisplayNameFallsBackToLogin() {
        // Regression: name()!! crashed for accounts that never set a name.
        val user = SearchResults.toUser(1, "U_1", null, "octocat")!!
        assertEquals("octocat", user.names)
        assertEquals("octocat", user.username)
        assertEquals("U_1", user.group)
        assertEquals("octocat", SearchResults.toUser(1, "U_1", "  ", "octocat")!!.names)
    }

    @Test
    fun userKeepsDisplayNameAndDropsIncompleteEdges() {
        assertEquals("The Octocat", SearchResults.toUser(0, "U", "The Octocat", "octocat")!!.names)
        assertNull(SearchResults.toUser(0, null, "x", "octocat"))
        assertNull(SearchResults.toUser(0, "U", "x", null))
    }

    @Test
    fun prependPageReturnsInsertedCountAndPutsLastItemFirst() {
        // Regression: notifyItemRangeInserted was given size - 1.
        val list = mutableListOf("old")
        val inserted = SearchResults.prependPage(list, listOf("a", "b", "c"))
        assertEquals(3, inserted)
        assertEquals(listOf("c", "b", "a", "old"), list)
        assertEquals(0, SearchResults.prependPage(list, emptyList()))
        assertEquals(4, list.size)
    }

    @Test
    fun nextPageOnlyWhenAtTopWithCursorAndBelowLimit() {
        assertTrue(SearchResults.shouldLoadNextPage(0, 5, "cursor"))
        assertFalse(SearchResults.shouldLoadNextPage(1, 5, "cursor"))
        assertFalse(SearchResults.shouldLoadNextPage(-1, 0, "cursor"))
        assertFalse(SearchResults.shouldLoadNextPage(0, 5, null))
        assertTrue(SearchResults.shouldLoadNextPage(0, SearchResults.MAX_USERS - 1, "c"))
        assertFalse(SearchResults.shouldLoadNextPage(0, SearchResults.MAX_USERS, "c"))
    }
}

package com.example.githubsearch.models

/**
 * Pure (Android-free) logic behind the two search screens: turning GraphQL
 * search edges into list items and deciding when to fetch the next page.
 * Kept here so it can be unit-tested on the JVM without network access.
 */
object SearchResults {

    /** Users fetched per GraphQL request. */
    const val USER_PAGE_SIZE = 5

    /** Infinite scroll stops once this many users are loaded. */
    const val MAX_USERS = 50

    /**
     * A user edge from the GraphQL response. GitHub returns `name = null` for
     * accounts that never set a display name; the login is shown instead.
     * Returns null for an edge without id or login (e.g. an Organization node
     * that did not match the `User` fragment).
     */
    fun toUser(index: Int, id: String?, name: String?, login: String?): GithubUser? {
        if (id == null || login == null) return null
        return GithubUser(index, id, if (name.isNullOrBlank()) login else name, 7, login)
    }

    /**
     * A repository edge. `description` is optional on GitHub, so null becomes "".
     */
    fun toRepo(index: Int, id: String?, name: String?, ownerLogin: String?, description: String?): GithubRepo? {
        if (id == null || name == null || ownerLogin == null) return null
        return GithubRepo(index, id, name, description ?: "", ownerLogin)
    }

    /**
     * Inserts [page] at the top of [list] in the order the screens use (each new
     * item goes to position 0) and returns how many items were inserted, which is
     * the count to pass to `notifyItemRangeInserted(0, count)`.
     */
    fun <T> prependPage(list: MutableList<T>, page: List<T>): Int {
        page.forEach { list.add(0, it) }
        return page.size
    }

    /**
     * Whether the user list should fetch the next page: the scroll settled with
     * the first item fully visible, there is a cursor to continue from, and the
     * list has not reached [MAX_USERS].
     */
    fun shouldLoadNextPage(firstCompletelyVisible: Int, loaded: Int, cursor: String?): Boolean =
        firstCompletelyVisible == 0 && loaded < MAX_USERS && cursor != null
}

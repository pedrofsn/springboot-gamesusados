package br.com.jogosusados.security

import org.junit.Assert
import org.junit.Test
import org.springframework.util.AntPathMatcher

class AntPathMatcherTests {

    @Test
    fun twoAsterisks() {
        val pattern = "/images/games/**"

        val matcher = AntPathMatcher()
        val matching = { path: String -> matcher.match(pattern, path) }

        Assert.assertTrue(matching("/images/games"))
        Assert.assertTrue(matching("/images/games/4"))
        Assert.assertTrue(matching("/images/games/"))
        Assert.assertTrue(matching("/images/games/abc"))
        Assert.assertTrue(matching("/images/games/abc/"))
        Assert.assertTrue(matching("/images/games/abc/update"))

        Assert.assertFalse(matching("/api/bala"))
    }

    @Test
    fun oneAsterisk() {
        val pattern = "/images/games/*"

        val matcher = AntPathMatcher()
        val matching = { path: String -> matcher.match(pattern, path) }

        Assert.assertTrue(matching("/images/games/"))
        Assert.assertTrue(matching("/images/games/abc"))

        Assert.assertFalse(matching("/api/bala"))
        Assert.assertFalse(matching("/images/games"))
        Assert.assertFalse(matching("/images/games/abc/"))
        Assert.assertFalse(matching("/images/games/abc/update"))
    }
}
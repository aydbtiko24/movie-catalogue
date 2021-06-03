package com.dicodingjetpackpro.moviecatalogue.sharedtest

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicodingjetpackpro.moviecatalogue.domain.models.Movie
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow

object Dummy {

    val movies = listOf(
        Movie(
            id = 460465,
            title = "Mortal Kombat",
            overview = "Washed-up MMA fighter Cole Young, unaware of his heritage, and hunted by Emperor Shang Tsung's best warrior, Sub-Zero, seeks out and trains with Earth's greatest champions as he prepares to stand against the enemies of Outworld in a high stakes battle for the universe.",
            backdropUrl = "/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg",
            posterUrl = "/6Wdl9N6dL0Hi0T1qJLWSz6gMLbd.jpg",
            readableDate = "2021-04-07",
            voteAverage = 8.0
        ),
        Movie(
            id = 399566,
            title = "Godzilla vs. Kong",
            overview = "In a time when monsters walk the Earth, humanity’s fight for its future sets Godzilla and Kong on a collision course that will see the two most powerful forces of nature on the planet collide in a spectacular battle for the ages.",
            backdropUrl = "/inJjDhCjfhh3RtrJWBmmDqeuSYC.jpg",
            posterUrl = "/pgqgaUx1cJb5oZQQ5v0tNARCeBp.jpg",
            readableDate = "2021-03-24",
            voteAverage = 8.2
        ),
        Movie(
            id = 615457,
            title = "Nobody",
            overview = "Hutch Mansell, a suburban dad, overlooked husband, nothing neighbor — a \\\"nobody.\\\" When two thieves break into his home one night, Hutch's unknown long-simmering rage is ignited and propels him on a brutal path that will uncover dark secrets he fought to leave behind.",
            backdropUrl = "/6zbKgwgaaCyyBXE4Sun4oWQfQmi.jpg",
            posterUrl = "/oBgWY00bEFeZ9N25wWVyuQddbAo.jpg",
            readableDate = "2021-03-18",
            voteAverage = 8.5
        ),
        Movie(
            id = 791373,
            title = "Zack Snyder's Justice League",
            overview = "Determined to ensure Superman's ultimate sacrifice was not in vain, Bruce Wayne aligns forces with Diana Prince with plans to recruit a team of metahumans to protect the world from an approaching threat of catastrophic proportions.",
            backdropUrl = "/pcDc2WJAYGJTTvRSEIpRZwM3Ola.jpg",
            posterUrl = "/tnAuB8q5vv7Ax9UAEje5Xi4BXik.jpg",
            readableDate = "2021-03-18",
            voteAverage = 8.5
        ),
        Movie(
            id = 632357,
            title = "The Unholy",
            overview = "Alice, a young hearing-impaired girl who, after a supposed visitation from the Virgin Mary, is inexplicably able to hear, speak and heal the sick. As word spreads and people from near and far flock to witness her miracles, a disgraced journalist hoping to revive his career visits the small New England town to investigate. When terrifying events begin to happen all around, he starts to question if these phenomena are the works of the Virgin Mary or something much more sinister.",
            backdropUrl = "/zDq2pwPyt4xwSFHKUoNN2LohDWj.jpg",
            posterUrl = "/b4gYVcl8pParX8AjkN90iQrWrWO.jpg",
            readableDate = "2021-03-31",
            voteAverage = 5.8
        ),
        Movie(
            id = 615678,
            title = "Thunder Force",
            overview = "In a world where supervillains are commonplace, two estranged childhood best friends reunite after one devises a treatment that gives them powers to protect their city.",
            backdropUrl = "/z7HLq35df6ZpRxdMAE0qE3Ge4SJ.jpg",
            posterUrl = "/duK11VQd4UPDa7UJrgrGx90xJOx.jpg",
            readableDate = "2021-04-09",
            voteAverage = 5.8
        ),
        Movie(
            id = 634528,
            title = "The Marksman",
            overview = "Jim Hanson’s quiet life is suddenly disturbed by two people crossing the US/Mexico border – a woman and her young son – desperate to flee a Mexican cartel. After a shootout leaves the mother dead, Jim becomes the boy’s reluctant defender. He embraces his role as Miguel’s protector and will stop at nothing to get him to safety, as they go on the run from the relentless assassins.",
            backdropUrl = "/5Zv5KmgZzdIvXz2KC3n0MyecSNL.jpg",
            posterUrl = "/6vcDalR50RWa309vBH1NLmG2rjQ.jpg",
            readableDate = "2021-01-15",
            voteAverage = 7.1
        ),
        Movie(
            id = 412656,
            title = "Chaos Walking",
            overview = "Two unlikely companions embark on a perilous adventure through the badlands of an unexplored planet as they try to escape a dangerous and disorienting reality, where all inner thoughts are seen and heard by everyone.",
            backdropUrl = "/ovggmAOu1IbPGTQE8lg4lBasNC7.jpg",
            posterUrl = "/9kg73Mg8WJKlB9Y2SAJzeDKAnuB.jpg",
            readableDate = "2021-02-24",
            voteAverage = 7.4
        ),
        Movie(
            id = 527774,
            title = "Raya and the Last Dragon",
            overview = "Long ago, in the fantasy world of Kumandra, humans and dragons lived together in harmony. But when an evil force threatened the land, the dragons sacrificed themselves to save humanity. Now, 500 years later, that same evil has returned and it’s up to a lone warrior, Raya, to track down the legendary last dragon to restore the fractured land and its divided people.",
            backdropUrl = "/7prYzufdIOy1KCTZKVWpjBFqqNr.jpg",
            posterUrl = "/lPsD10PP4rgUGiGR4CCXA6iY0QQ.jpg",
            readableDate = "2021-03-03",
            voteAverage = 8.3
        ),
        Movie(
            id = 635302,
            title = "Demon Slayer the Movie: Mugen Train",
            overview = "Tanjirō Kamado, joined with Inosuke Hashibira, a boy raised by boars who wears a boar's head, and Zenitsu Agatsuma, a scared boy who reveals his true power when he sleeps, boards the Infinity Train on a new mission with the Fire Hashira, Kyōjurō Rengoku, to defeat a demon who has been tormenting the people and killing the demon slayers who oppose it!",
            backdropUrl = "/7prYzufdIOy1KCTZKVWpjBFqqNr.jpg",
            posterUrl = "/m9cn5mhW519QKr1YGpGxNWi98VJ.jpg",
            readableDate = "2020-10-16",
            voteAverage = 8.1
        ),
        Movie(
            id = 663558,
            title = "New Gods: Nezha Reborn",
            overview = "3000 years after the boy-god Nezha conquers the Dragon King then disappears in mythological times, he returns as an ordinary man to find his own path to becoming a true hero.",
            backdropUrl = "/ynZs1Kc8agOB1cK2bh8NB2xeFrg.jpg",
            posterUrl = "/6goDkAD6J3br81YMQf0Gat8Bqjy.jpg",
            readableDate = "2021-02-06",
            voteAverage = 8.9
        )
    )
    val tvShows = listOf(
        TvShow(
            id = 88396,
            title = "The Falcon and the Winter Soldier",
            overview = "Following the events of “Avengers: Endgame”, the Falcon, Sam Wilson and the Winter Soldier, Bucky Barnes team up in a global adventure that tests their abilities, and their patience.",
            backdropUrl = "/b0WmHGc8LHTdGCVzxRb3IBMur57.jpg",
            posterUrl = "/6kbAMLteGO8yyewYau6bJ683sw7.jpg",
            readableDate = "2021-03-19",
            voteAverage = 7.9
        ),
        TvShow(
            id = 71712,
            title = "The Good Doctor",
            overview = "A young surgeon with Savant syndrome is recruited into the surgical unit of a prestigious hospital. The question will arise: can a person who doesn't have the ability to relate to people actually save their lives",
            backdropUrl = "/mZjZgY6ObiKtVuKVDrnS9VnuNlE.jpg",
            posterUrl = "/6tfT03sGp9k4c0J3dypjrI8TSAI.jpg",
            readableDate = "2017-09-25",
            voteAverage = 8.6
        ),
        TvShow(
            id = 79008,
            title = "Luis Miguel: The Series",
            overview = "The series dramatizes the life story of Mexican superstar singer Luis Miguel, who has captivated audiences in Latin America and beyond for decades.",
            backdropUrl = "/wkyzeBBKLhSg1Oqhky5yoiFF2hG.jpg",
            posterUrl = "/34FaY8qpjBAVysSfrJ1l7nrAQaD.jpg",
            readableDate = "2018-04-22",
            voteAverage = 8.1
        ),
        TvShow(
            id = 60735,
            title = "The Flash",
            overview = "After a particle accelerator causes a freak storm, CSI Investigator Barry Allen is struck by lightning and falls into a coma. Months later he awakens with the power of super speed, granting him the ability to move through Central City like an unseen guardian angel. Though initially excited by his newfound powers, Barry is shocked to discover he is not the only \\\"meta-human\\\" who was created in the wake of the accelerator explosion -- and not everyone is using their new powers for good. Barry partners with S.T.A.R. Labs and dedicates his life to protect the innocent. For now, only a few close friends and associates know that Barry is literally the fastest man alive, but it won't be long before the world learns what Barry Allen has become...The Flash.",
            backdropUrl = "/z59kJfcElR9eHO9rJbWp4qWMuee.jpg",
            posterUrl = "/lJA2RCMfsWoskqlQhXPSLFQGXEJ.jpg",
            readableDate = "2014-10-07",
            voteAverage = 7.7
        ),
        TvShow(
            id = 65820,
            title = "Van Helsing",
            overview = "Vanessa Helsing, the daughter of famous vampire hunter and Dracula nemesis Abraham Van Helsing is resurrected five years in the future to find out that vampires have taken over the world and that she possesses unique power over them. She is humanity’s last hope to lead an offensive to take back what has been lost.",
            backdropUrl = "/5VltHQJXdmbSD6gEJw3R8R1Kbmc.jpg",
            posterUrl = "/r8ODGmfNbZQlNhiJl2xQENE2jsk.jpg",
            readableDate = "2016-09-23",
            voteAverage = 6.9
        ),
        TvShow(
            id = 1416,
            title = "Grey's Anatomy",
            overview = "Follows the personal and professional lives of a group of doctors at Seattle’s Grey Sloan Memorial Hospital.",
            backdropUrl = "/edmk8xjGBsYVIf4QtLY9WMaMcXZ.jpg",
            posterUrl = "/clnyhPqj1SNgpAdeSS6a6fwE6Bo.jpg",
            readableDate = "2005-03-27",
            voteAverage = 8.2
        ),
        TvShow(
            id = 95557,
            title = "Invincible",
            overview = "Mark Grayson is a normal teenager except for the fact that his father is the most powerful superhero on the planet. Shortly after his seventeenth birthday, Mark begins to develop powers of his own and enters into his father’s tutelage.",
            backdropUrl = "/6UH52Fmau8RPsMAbQbjwN3wJSCj.jpg",
            posterUrl = "/yDWJYRAwMNKbIYT8ZB33qy84uzO.jpg",
            readableDate = "2021-03-26",
            voteAverage = 8.9
        ),
        TvShow(
            id = 69050,
            title = "Riverdale",
            overview = "Set in the present, the series offers a bold, subversive take on Archie, Betty, Veronica and their friends, exploring the surreality of small-town life, the darkness and weirdness bubbling beneath Riverdale’s wholesome facade.",
            backdropUrl = "/qZtAf4Z1lazGQoYVXiHOrvLr5lI.jpg",
            posterUrl = "/wRbjVBdDo5qHAEOVYoMWpM58FSA.jpg",
            readableDate = "2017-01-26",
            voteAverage = 8.6
        ),
        TvShow(
            id = 63174,
            title = "Lucifer",
            overview = "Bored and unhappy as the Lord of Hell, Lucifer Morningstar abandoned his throne and retired to Los Angeles, where he has teamed up with LAPD detective Chloe Decker to take down criminals. But the longer he's away from the underworld, the greater the threat that the worst of humanity could escape.",
            backdropUrl = "/ta5oblpMlEcIPIS2YGcq9XEkWK2.jpg",
            posterUrl = "/4EYPN5mVIhKLfxGruy7Dy41dTVn.jpg",
            readableDate = "2016-01-25",
            voteAverage = 8.5
        ),
        TvShow(
            id = 85271,
            title = "WandaVision",
            overview = "Wanda Maximoff and Vision—two super-powered beings living idealized suburban lives—begin to suspect that everything is not as it seems.",
            backdropUrl = "/57vVjteucIF3bGnZj6PmaoJRScw.jpg",
            posterUrl = "/glKDfE6btIRcVB5zrjspRIs4r52.jpg",
            readableDate = "2021-01-15",
            voteAverage = 8.4
        ),
        TvShow(
            id = 1402,
            title = "The Walking Dead",
            overview = "Sheriff's deputy Rick Grimes awakens from a coma to find a post-apocalyptic world dominated by flesh-eating zombies. He sets out to find his family and encounters many other survivors along the way.",
            backdropUrl = "/uro2Khv7JxlzXtLb8tCIbRhkb9E.jpg",
            posterUrl = "/rqeYMLryjcawh2JeRpCVUDXYM5b.jpg",
            readableDate = "2010-10-31",
            voteAverage = 8.1
        )
    )
    val dummyPagingMovieSource = object : PagingSource<Int, Movie>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
            return LoadResult.Page(
                data = movies,
                prevKey = null,
                nextKey = null
            )
        }

        override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            }
        }
    }
    val dummyPagingTvshowSource = object : PagingSource<Int, TvShow>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvShow> {
            return LoadResult.Page(
                data = tvShows,
                prevKey = null,
                nextKey = null
            )
        }

        override fun getRefreshKey(state: PagingState<Int, TvShow>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            }
        }
    }
}

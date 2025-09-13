package com.tvfootballhd.liveandstream.Model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class TeamResponse {
    public ArrayList<Object> errors;
    public String get;
    public Paging paging;
    public Parameters parameters;
    public Response response;
    public int results;

    public ArrayList<Object> getErrors() {
        return this.errors;
    }

    public String getGet() {
        return this.get;
    }

    public Paging getPaging() {
        return this.paging;
    }

    public Parameters getParameters() {
        return this.parameters;
    }

    public Response getResponse() {
        return this.response;
    }

    public int getResults() {
        return this.results;
    }

    public class _015 {
        public String percentage;
        public int total;

        public _015() {
        }

        public String getPercentage() {
            return this.percentage;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class _106120 {
        public String percentage;
        public int total;

        public _106120() {
        }

        public String getPercentage() {
            return this.percentage;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class _1630 {
        public String percentage;
        public int total;

        public _1630() {
        }

        public String getPercentage() {
            return this.percentage;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class _3145 {
        public String percentage;
        public int total;

        public _3145() {
        }

        public String getPercentage() {
            return this.percentage;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class _4660 {
        public String percentage;
        public int total;

        public _4660() {
        }

        public String getPercentage() {
            return this.percentage;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class _6175 {
        public String percentage;
        public int total;

        public _6175() {
        }

        public String getPercentage() {
            return this.percentage;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class _7690 {
        public String percentage;
        public int total;

        public _7690() {
        }

        public String getPercentage() {
            return this.percentage;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class _91105 {
        public String percentage;
        public int total;

        public _91105() {
        }

        public String getPercentage() {
            return this.percentage;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class Against {
        public Average average;
        public int away;
        public int home;
        public Minute minute;
        public Total total;

        public Against() {
        }

        public int getAway() {
            return this.away;
        }

        public int getHome() {
            return this.home;
        }

        public Average getAverage() {
            return this.average;
        }

        public Minute getMinute() {
            return this.minute;
        }

        public Total getTotal() {
            return this.total;
        }
    }

    public class Average {
        public String away;
        public String home;
        public String total;

        public Average() {
        }

        public String getAway() {
            return this.away;
        }

        public String getHome() {
            return this.home;
        }

        public String getTotal() {
            return this.total;
        }
    }

    public class Biggest {
        public Goals goals;
        public Loses loses;
        public Streak streak;
        public Wins wins;

        public Biggest() {
        }

        public Goals getGoals() {
            return this.goals;
        }

        public Loses getLoses() {
            return this.loses;
        }

        public Streak getStreak() {
            return this.streak;
        }

        public Wins getWins() {
            return this.wins;
        }
    }

    public class Cards {
        public Red red;
        public Yellow yellow;

        public Cards() {
        }

        public Red getRed() {
            return this.red;
        }

        public Yellow getYellow() {
            return this.yellow;
        }
    }

    public class CleanSheet {
        public int away;
        public int home;
        public int total;

        public CleanSheet() {
        }

        public int getAway() {
            return this.away;
        }

        public int getHome() {
            return this.home;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class Draws {
        public int away;
        public int home;
        public int total;

        public Draws() {
        }

        public int getAway() {
            return this.away;
        }

        public int getHome() {
            return this.home;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class FailedToScore {
        public int away;
        public int home;
        public int total;

        public FailedToScore() {
        }

        public int getAway() {
            return this.away;
        }

        public int getHome() {
            return this.home;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class Fixtures {
        public Draws draws;
        public Loses loses;
        public Played played;
        public Wins wins;

        public Fixtures() {
        }

        public Draws getDraws() {
            return this.draws;
        }

        public Loses getLoses() {
            return this.loses;
        }

        public Played getPlayed() {
            return this.played;
        }

        public Wins getWins() {
            return this.wins;
        }
    }

    public class For {
        public Average average;
        public int away;
        public int home;
        public Minute minute;
        public Total total;

        public For() {
        }

        public int getAway() {
            return this.away;
        }

        public int getHome() {
            return this.home;
        }

        public Average getAverage() {
            return this.average;
        }

        public Minute getMinute() {
            return this.minute;
        }

        public Total getTotal() {
            return this.total;
        }
    }

    public class Goals {
        public Against against;
        @SerializedName("for")
        public For myfor;

        public Goals() {
        }

        public Against getAgainst() {
            return this.against;
        }

        public For getMyfor() {
            return this.myfor;
        }
    }

    public class League {
        public String country;
        public String flag;
        public int id;
        public String logo;
        public String name;
        public int season;

        public League() {
        }

        public String getCountry() {
            return this.country;
        }

        public String getFlag() {
            return this.flag;
        }

        public int getId() {
            return this.id;
        }

        public String getLogo() {
            return this.logo;
        }

        public String getName() {
            return this.name;
        }

        public int getSeason() {
            return this.season;
        }
    }

    public class Lineup {
        public String formation;
        public int played;

        public Lineup() {
        }

        public String getFormation() {
            return this.formation;
        }

        public int getPlayed() {
            return this.played;
        }
    }

    public class Loses {
        public Object away;
        public String home;
        public int total;

        public Loses() {
        }

        public Object getAway() {
            return this.away;
        }

        public String getHome() {
            return this.home;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class Minute {
        @SerializedName("0-15")
        public _015 fifteen;
        @SerializedName("31-45")
        public _3145 fortiefive;
        @SerializedName("76-90")
        public _7690 nineTy;
        @SerializedName("106-120")
        public _106120 oneTwenty;
        @SerializedName("91-105")
        public _91105 oneZeroFive;
        @SerializedName("61-75")
        public _6175 seventiFive;
        @SerializedName("46-60")
        public _4660 sixtey;
        @SerializedName("16-30")
        public _1630 thirty;

        public Minute() {
        }

        public _015 getFifteen() {
            return this.fifteen;
        }

        public _106120 getOneTwenty() {
            return this.oneTwenty;
        }

        public _1630 getThirty() {
            return this.thirty;
        }

        public _3145 getFortiefive() {
            return this.fortiefive;
        }

        public _4660 getSixtey() {
            return this.sixtey;
        }

        public _6175 getSeventiFive() {
            return this.seventiFive;
        }

        public _7690 getNineTy() {
            return this.nineTy;
        }

        public _91105 getOneZeroFive() {
            return this.oneZeroFive;
        }
    }

    public class Missed {
        public String percentage;
        public int total;

        public Missed() {
        }

        public String getPercentage() {
            return this.percentage;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class Paging {
        public int current;
        public int total;

        public Paging() {
        }

        public int getCurrent() {
            return this.current;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class Parameters {
        public String league;
        public String season;
        public String team;

        public Parameters() {
        }

        public String getLeague() {
            return this.league;
        }

        public String getSeason() {
            return this.season;
        }

        public String getTeam() {
            return this.team;
        }
    }

    public class Penalty {
        public Missed missed;
        public Scored scored;
        public int total;

        public Penalty() {
        }

        public Missed getMissed() {
            return this.missed;
        }

        public Scored getScored() {
            return this.scored;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class Played {
        public int away;
        public int home;
        public int total;

        public Played() {
        }

        public int getAway() {
            return this.away;
        }

        public int getHome() {
            return this.home;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class Red {
        @SerializedName("0-15")
        public _015 fifteen;
        @SerializedName("31-45")
        public _3145 fortiefive;
        @SerializedName("76-90")
        public _7690 nineTy;
        @SerializedName("106-120")
        public _106120 oneTwenty;
        @SerializedName("91-105")
        public _91105 oneZeroFive;
        @SerializedName("61-75")
        public _6175 seventiFive;
        @SerializedName("46-60")
        public _4660 sixtey;
        @SerializedName("16-30")
        public _1630 thirty;

        public Red() {
        }

        public _015 getFifteen() {
            return this.fifteen;
        }

        public _106120 getOneTwenty() {
            return this.oneTwenty;
        }

        public _1630 getThirty() {
            return this.thirty;
        }

        public _3145 getFortiefive() {
            return this.fortiefive;
        }

        public _4660 getSixtey() {
            return this.sixtey;
        }

        public _6175 getSeventiFive() {
            return this.seventiFive;
        }

        public _7690 getNineTy() {
            return this.nineTy;
        }

        public _91105 getOneZeroFive() {
            return this.oneZeroFive;
        }
    }

    public class Response {
        public Biggest biggest;
        public Cards cards;
        public CleanSheet clean_sheet;
        public FailedToScore failed_to_score;
        public Fixtures fixtures;
        public String form;
        public Goals goals;
        public League league;
        public ArrayList<Lineup> lineups;
        public Penalty penalty;
        public Team team;

        public Response() {
        }

        public Biggest getBiggest() {
            return this.biggest;
        }

        public Cards getCards() {
            return this.cards;
        }

        public CleanSheet getClean_sheet() {
            return this.clean_sheet;
        }

        public FailedToScore getFailed_to_score() {
            return this.failed_to_score;
        }

        public Fixtures getFixtures() {
            return this.fixtures;
        }

        public String getForm() {
            return this.form;
        }

        public Goals getGoals() {
            return this.goals;
        }

        public League getLeague() {
            return this.league;
        }

        public ArrayList<Lineup> getLineups() {
            return this.lineups;
        }

        public Penalty getPenalty() {
            return this.penalty;
        }

        public Team getTeam() {
            return this.team;
        }
    }

    public class Scored {
        public String percentage;
        public int total;

        public Scored() {
        }

        public String getPercentage() {
            return this.percentage;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class Streak {
        public int draws;
        public int loses;
        public int wins;

        public Streak() {
        }

        public int getDraws() {
            return this.draws;
        }

        public int getLoses() {
            return this.loses;
        }

        public int getWins() {
            return this.wins;
        }
    }

    public class Team {
        public int id;
        public String logo;
        public String name;

        public Team() {
        }

        public int getId() {
            return this.id;
        }

        public String getLogo() {
            return this.logo;
        }

        public String getName() {
            return this.name;
        }
    }

    public class Total {
        public int away;
        public int home;
        public int total;

        public Total() {
        }

        public int getAway() {
            return this.away;
        }

        public int getHome() {
            return this.home;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class Wins {
        public String away;
        public String home;
        public int total;

        public Wins() {
        }

        public String getAway() {
            return this.away;
        }

        public String getHome() {
            return this.home;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class Yellow {
        @SerializedName("0-15")
        public _015 fifteen;
        @SerializedName("31-45")
        public _3145 fortiefive;
        @SerializedName("76-90")
        public _7690 nineTy;
        @SerializedName("106-120")
        public _106120 oneTwenty;
        @SerializedName("91-105")
        public _91105 oneZeroFive;
        @SerializedName("61-75")
        public _6175 seventiFive;
        @SerializedName("46-60")
        public _4660 sixtey;
        @SerializedName("16-30")
        public _1630 thirty;

        public Yellow() {
        }

        public _015 getFifteen() {
            return this.fifteen;
        }

        public _106120 getOneTwenty() {
            return this.oneTwenty;
        }

        public _1630 getThirty() {
            return this.thirty;
        }

        public _3145 getFortiefive() {
            return this.fortiefive;
        }

        public _4660 getSixtey() {
            return this.sixtey;
        }

        public _6175 getSeventiFive() {
            return this.seventiFive;
        }

        public _7690 getNineTy() {
            return this.nineTy;
        }

        public _91105 getOneZeroFive() {
            return this.oneZeroFive;
        }
    }
}

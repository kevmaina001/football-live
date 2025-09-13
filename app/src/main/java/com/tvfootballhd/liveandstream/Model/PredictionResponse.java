package com.tvfootballhd.liveandstream.Model;

import java.util.ArrayList;
import java.util.Date;

public class PredictionResponse {
    public ArrayList<Object> errors;
    public String get;
    public Paging paging;
    public Parameters parameters;
    public ArrayList<Response> response;
    public int results;

    public String getGet() {
        return this.get;
    }

    public Parameters getParameters() {
        return this.parameters;
    }

    public ArrayList<Object> getErrors() {
        return this.errors;
    }

    public int getResults() {
        return this.results;
    }

    public Paging getPaging() {
        return this.paging;
    }

    public ArrayList<Response> getResponse() {
        return this.response;
    }

    public class Parameters {
        public Parameters() {
        }
    }

    public class Paging {
        public Paging() {
        }
    }

    public class Response {
        public Comparison comparison;
        public ArrayList<H2h> h2h;
        public League league;
        public Predictions predictions;
        public Teams teams;

        public Response() {
        }

        public Predictions getPredictions() {
            return this.predictions;
        }

        public League getLeague() {
            return this.league;
        }

        public Teams getTeams() {
            return this.teams;
        }

        public Comparison getComparison() {
            return this.comparison;
        }

        public ArrayList<H2h> getH2h() {
            return this.h2h;
        }
    }

    public class Comparison {
        public Att att;
        public Def def;
        public Form form;
        public Goals goals;
        public H2h h2h;
        public PoissonDistribution poisson_distribution;
        public Total total;

        public Comparison() {
        }

        public Form getForm() {
            return this.form;
        }

        public Att getAtt() {
            return this.att;
        }

        public Def getDef() {
            return this.def;
        }

        public PoissonDistribution getPoisson_distribution() {
            return this.poisson_distribution;
        }

        public H2h getH2h() {
            return this.h2h;
        }

        public Goals getGoals() {
            return this.goals;
        }

        public Total getTotal() {
            return this.total;
        }

        public class Form {
            String away;
            String home;

            public Form() {
            }

            public String getHome() {
                return this.home;
            }

            public String getAway() {
                return this.away;
            }
        }

        public class Att {
            String away;
            String home;

            public Att() {
            }

            public String getHome() {
                return this.home;
            }

            public String getAway() {
                return this.away;
            }
        }

        public class Def {
            String away;
            String home;

            public Def() {
            }

            public String getHome() {
                return this.home;
            }

            public String getAway() {
                return this.away;
            }
        }

        public class PoissonDistribution {
            String away;
            String home;

            public PoissonDistribution() {
            }

            public String getHome() {
                return this.home;
            }

            public String getAway() {
                return this.away;
            }
        }

        public class H2h {
            String away;
            String home;

            public H2h() {
            }

            public String getHome() {
                return this.home;
            }

            public String getAway() {
                return this.away;
            }
        }

        public class Goals {
            String away;
            String home;

            public Goals() {
            }

            public String getHome() {
                return this.home;
            }

            public String getAway() {
                return this.away;
            }
        }

        public class Total {
            String away;
            String home;

            public Total() {
            }

            public String getHome() {
                return this.home;
            }

            public String getAway() {
                return this.away;
            }
        }
    }

    public class Predictions {
        public String advice;
        public Goals goals;
        public Percent percent;
        public String under_over;
        public boolean win_or_draw;
        public Winner winner;

        public Predictions() {
        }

        public Winner getWinner() {
            return this.winner;
        }

        public boolean isWin_or_draw() {
            return this.win_or_draw;
        }

        public String getUnder_over() {
            return this.under_over;
        }

        public Goals getGoals() {
            return this.goals;
        }

        public String getAdvice() {
            return this.advice;
        }

        public Percent getPercent() {
            return this.percent;
        }
    }

    public class Teams {
        public Away away;
        public Home home;

        public Teams() {
        }

        public Home getHome() {
            return this.home;
        }

        public Away getAway() {
            return this.away;
        }
    }

    public class League {
        public Biggest biggest;
        public Cards cards;
        public CleanSheet clean_sheet;
        public String country;
        public FailedToScore failed_to_score;
        public Fixtures fixtures;
        public String flag;
        public String form;
        public Goals goals;
        public int id;
        public ArrayList<Object> lineups;
        public String logo;
        public String name;
        public Penalty penalty;
        public String round;
        public int season;

        public League() {
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public String getCountry() {
            return this.country;
        }

        public String getLogo() {
            return this.logo;
        }

        public String getFlag() {
            return this.flag;
        }

        public int getSeason() {
            return this.season;
        }

        public String getForm() {
            return this.form;
        }

        public Fixtures getFixtures() {
            return this.fixtures;
        }

        public Goals getGoals() {
            return this.goals;
        }

        public Biggest getBiggest() {
            return this.biggest;
        }

        public CleanSheet getClean_sheet() {
            return this.clean_sheet;
        }

        public FailedToScore getFailed_to_score() {
            return this.failed_to_score;
        }

        public Penalty getPenalty() {
            return this.penalty;
        }

        public ArrayList<Object> getLineups() {
            return this.lineups;
        }

        public Cards getCards() {
            return this.cards;
        }

        public String getRound() {
            return this.round;
        }
    }

    public class Against {
        public String average;
        public int away;
        public int home;
        public Minute minute;
        public int total;

        public Against() {
        }

        public int getTotal() {
            return this.total;
        }

        public String getAverage() {
            return this.average;
        }

        public Minute getMinute() {
            return this.minute;
        }

        public int getHome() {
            return this.home;
        }

        public int getAway() {
            return this.away;
        }
    }

    public class Att {
        public String away;
        public String home;

        public Att() {
        }

        public String getHome() {
            return this.home;
        }

        public String getAway() {
            return this.away;
        }
    }

    public class Average {
        public String away;
        public String home;
        public String total;

        public Average() {
        }

        public String getHome() {
            return this.home;
        }

        public String getAway() {
            return this.away;
        }

        public String getTotal() {
            return this.total;
        }
    }

    public class Away {
        public int id;
        public Last5 last_5;
        public League league;
        public String logo;
        public String name;
        public boolean winner;

        public Away() {
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public String getLogo() {
            return this.logo;
        }

        public Last5 getLast_5() {
            return this.last_5;
        }

        public League getLeague() {
            return this.league;
        }

        public boolean isWinner() {
            return this.winner;
        }

        public class League {
            public League() {
            }
        }
    }

    public class Biggest {
        public Goals goals;
        public Loses loses;
        public Streak streak;
        public Wins wins;

        public Biggest() {
        }
    }

    public class Cards {
        public Red red;
        public Yellow yellow;

        public Cards() {
        }
    }

    public class CleanSheet {
        public int away;
        public int home;
        public int total;

        public CleanSheet() {
        }
    }

    public class Def {
        public String away;
        public String home;

        public Def() {
        }
    }

    public class Draws {
        public int away;
        public int home;
        public int total;

        public Draws() {
        }
    }

    public class Extratime {
        public Object away;
        public Object home;

        public Extratime() {
        }
    }

    public class FailedToScore {
        public int away;
        public int home;
        public int total;

        public FailedToScore() {
        }
    }

    public class Fixture {
        public Date date;
        public int id;
        public Periods periods;
        public String referee;
        public Status status;
        public int timestamp;
        public String timezone;
        public Venue venue;

        public Fixture() {
        }

        public int getId() {
            return this.id;
        }

        public String getReferee() {
            return this.referee;
        }

        public String getTimezone() {
            return this.timezone;
        }

        public Date getDate() {
            return this.date;
        }

        public int getTimestamp() {
            return this.timestamp;
        }

        public Periods getPeriods() {
            return this.periods;
        }

        public Venue getVenue() {
            return this.venue;
        }

        public Status getStatus() {
            return this.status;
        }
    }

    public class Fixtures {
        public Draws draws;
        public Loses loses;
        public Played played;
        public Wins wins;

        public Fixtures() {
        }
    }

    public class For {
        public String average;
        public int away;
        public int home;
        public Minute minute;
        public int total;

        public For() {
        }
    }

    public class Form {
        public String away;
        public String home;

        public Form() {
        }
    }

    public class Fulltime {
        public int away;
        public int home;

        public Fulltime() {
        }
    }

    public class Goals {
        public String away;
        public String home;

        public Goals() {
        }

        public String getHome() {
            return this.home;
        }

        public void setHome(String str) {
            this.home = str;
        }

        public String getAway() {
            return this.away;
        }

        public void setAway(String str) {
            this.away = str;
        }
    }

    public class H2h {
        public String away;
        public Fixture fixture;
        public Goals goals;
        public String home;
        public League league;
        public Score score;
        public Teams teams;

        public H2h() {
        }

        public class Goals {
            int away;
            int home;

            public Goals() {
            }

            public int getHome() {
                return this.home;
            }

            public int getAway() {
                return this.away;
            }
        }

        public String getHome() {
            return this.home;
        }

        public String getAway() {
            return this.away;
        }

        public Fixture getFixture() {
            return this.fixture;
        }

        public League getLeague() {
            return this.league;
        }

        public Teams getTeams() {
            return this.teams;
        }

        public Goals getGoals() {
            return this.goals;
        }

        public Score getScore() {
            return this.score;
        }
    }

    public class Halftime {
        public int away;
        public int home;

        public Halftime() {
        }
    }

    public class Home {
        public int id;
        public Last5 last_5;
        public League league;
        public String logo;
        public String name;
        public boolean winner;

        public Home() {
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public String getLogo() {
            return this.logo;
        }

        public Last5 getLast_5() {
            return this.last_5;
        }

        public League getLeague() {
            return this.league;
        }

        public boolean isWinner() {
            return this.winner;
        }

        public class League {
            public League() {
            }
        }
    }

    public class Last5 {
        public Last5() {
        }
    }

    public class Loses {
        public int away;
        public int home;
        public int total;

        public Loses() {
        }
    }

    public class Minute {
        public Minute() {
        }
    }

    public class Missed {
        public String percentage;
        public int total;

        public Missed() {
        }
    }

    public class Penalty {
        public Object away;
        public Object home;
        public Missed missed;
        public Scored scored;
        public int total;

        public Penalty() {
        }

        public Scored getScored() {
            return this.scored;
        }

        public Missed getMissed() {
            return this.missed;
        }

        public int getTotal() {
            return this.total;
        }

        public Object getHome() {
            return this.home;
        }

        public Object getAway() {
            return this.away;
        }
    }

    public class Percent {
        public String away;
        public String draw;
        public String home;

        public Percent() {
        }

        public String getHome() {
            return this.home;
        }

        public String getDraw() {
            return this.draw;
        }

        public String getAway() {
            return this.away;
        }
    }

    public class Periods {
        public int first;
        public int second;

        public Periods() {
        }
    }

    public class Played {
        public int away;
        public int home;
        public int total;

        public Played() {
        }
    }

    public class PoissonDistribution {
        public String away;
        public String home;

        public PoissonDistribution() {
        }
    }

    public class Red {
        public Red() {
        }
    }

    public class Score {
        public Extratime extratime;
        public Fulltime fulltime;
        public Halftime halftime;
        public Penalty penalty;

        public Score() {
        }
    }

    public class Scored {
        public String percentage;
        public int total;

        public Scored() {
        }
    }

    public class Status {
        public int elapsed;
        public String mylong;
        public String myshort;

        public Status() {
        }
    }

    public class Streak {
        public int draws;
        public int loses;
        public int wins;

        public Streak() {
        }
    }

    public class Total {
        public int away;
        public int home;
        public int total;

        public Total() {
        }
    }

    public class Venue {
        public Object city;
        public Object id;
        public String name;

        public Venue() {
        }
    }

    public class Winner {
        public String comment;
        public int id;
        public String name;

        public Winner() {
        }
    }

    public class Wins {
        public int away;
        public int home;
        public int total;

        public Wins() {
        }
    }

    public class Yellow {
        public Yellow() {
        }
    }
}

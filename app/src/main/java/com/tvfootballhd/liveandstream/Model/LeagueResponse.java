package com.tvfootballhd.liveandstream.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class LeagueResponse implements Serializable {
    public ArrayList<Object> errors;
    public String get;
    public Paging paging;
    public ArrayList<Object> parameters;
    public ArrayList<Response> response;
    public int results;

    public String getGet() {
        return this.get;
    }

    public ArrayList<Object> getParameters() {
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

    public class Country implements Serializable {
        public String code;
        public String flag;
        public String name;

        public Country() {
        }

        public String getName() {
            return this.name;
        }

        public String getCode() {
            return this.code;
        }

        public String getFlag() {
            return this.flag;
        }
    }

    public class Coverage implements Serializable {
        public Fixtures fixtures;
        public boolean injuries;
        public boolean odds;
        public boolean players;
        public boolean predictions;
        public boolean standings;
        public boolean top_assists;
        public boolean top_cards;
        public boolean top_scorers;

        public Coverage() {
        }

        public Fixtures getFixtures() {
            return this.fixtures;
        }

        public boolean isStandings() {
            return this.standings;
        }

        public boolean isPlayers() {
            return this.players;
        }

        public boolean isTop_scorers() {
            return this.top_scorers;
        }

        public boolean isTop_assists() {
            return this.top_assists;
        }

        public boolean isTop_cards() {
            return this.top_cards;
        }

        public boolean isInjuries() {
            return this.injuries;
        }

        public boolean isPredictions() {
            return this.predictions;
        }

        public boolean isOdds() {
            return this.odds;
        }
    }

    public class Fixtures implements Serializable {
        public boolean events;
        public boolean lineups;
        public boolean statistics_fixtures;
        public boolean statistics_players;

        public Fixtures() {
        }

        public boolean isEvents() {
            return this.events;
        }

        public boolean isLineups() {
            return this.lineups;
        }

        public boolean isStatistics_fixtures() {
            return this.statistics_fixtures;
        }

        public boolean isStatistics_players() {
            return this.statistics_players;
        }
    }

    public class League implements Serializable {
        public int id;
        public String logo;
        public String name;
        public String type;

        public League() {
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public String getType() {
            return this.type;
        }

        public String getLogo() {
            return this.logo;
        }
    }

    public class Paging implements Serializable {
        public int current;
        public int total;

        public Paging() {
        }
    }

    public class Response implements Serializable {
        public Country country;
        public League league;
        public ArrayList<Season> seasons;

        public Response() {
        }

        public League getLeague() {
            return this.league;
        }

        public Country getCountry() {
            return this.country;
        }

        public ArrayList<Season> getSeasons() {
            return this.seasons;
        }
    }

    public class Season implements Serializable {
        public Coverage coverage;
        public boolean current;
        public String end;
        public String start;
        public int year;

        public Season() {
        }

        public int getYear() {
            return this.year;
        }

        public String getStart() {
            return this.start;
        }

        public String getEnd() {
            return this.end;
        }

        public boolean isCurrent() {
            return this.current;
        }

        public Coverage getCoverage() {
            return this.coverage;
        }
    }
}

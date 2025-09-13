package com.tvfootballhd.liveandstream.Model;

import java.util.ArrayList;

public class LineUpResponse {
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

    public class Coach {
        public int id;
        public String name;
        public String photo;

        public Coach() {
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public String getPhoto() {
            return this.photo;
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
        public String fixture;

        public Parameters() {
        }

        public String getFixture() {
            return this.fixture;
        }
    }

    public class Player {
        public String grid;
        public int id;
        public String name;
        public int number;
        public String pos;

        public Player() {
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public int getNumber() {
            return this.number;
        }

        public String getPos() {
            return this.pos;
        }

        public String getGrid() {
            return this.grid;
        }
    }

    public class Response {
        public Coach coach;
        public String formation;
        public ArrayList<StartXI> startXI;
        public ArrayList<Substitute> substitutes;
        public Team team;

        public Response() {
        }

        public Team getTeam() {
            return this.team;
        }

        public Coach getCoach() {
            return this.coach;
        }

        public String getFormation() {
            return this.formation;
        }

        public ArrayList<StartXI> getStartXI() {
            return this.startXI;
        }

        public ArrayList<Substitute> getSubstitutes() {
            return this.substitutes;
        }
    }

    public class StartXI {
        public Player player;

        public StartXI() {
        }

        public Player getPlayer() {
            return this.player;
        }
    }

    public class Substitute {
        public Player player;

        public Substitute() {
        }

        public Player getPlayer() {
            return this.player;
        }
    }

    public class Team {
        public Object colors;
        public int id;
        public String logo;
        public String name;

        public Team() {
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

        public Object getColors() {
            return this.colors;
        }
    }
}

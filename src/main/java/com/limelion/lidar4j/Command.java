package com.limelion.lidar4j;

public interface Command {

    static Command valueOf(String commandStr) {

        for (Request req : Request.values())
            if (req.getCommandStr().equals(commandStr))
                return req;

        for (Response res : Response.values())
            if (res.getCommandStr().equals(commandStr))
                return res;

        throw new IllegalArgumentException("No command exists with this value : " + commandStr);
    }

    String getCommandStr();

    enum Request implements Command {

        GET_SCAN_DATA("sRN LMDscandata");

        private String commandStr;

        Request(String commandStr) {

            this.commandStr = commandStr;
        }

        public String getCommandStr() {

            return commandStr;
        }
    }

    enum Response implements Command {

        GET_SCAN_DATA("sRA LMDscandata");

        private String commandStr;

        Response(String commandStr) {

            this.commandStr = commandStr;
        }

        public String getCommandStr() {

            return commandStr;
        }
    }
}

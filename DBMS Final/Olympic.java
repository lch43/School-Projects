//Landon Higinbotham
//LCH43
//CS1555

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Olympic {
    private static final String dbUsername = "USERNAME"; //Changed for confidential purposes.
    private static final String dbPassword = "PASSWORD"; //Changed for confidential purposes.
    private static final String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
    private static int auth_USER_ROLE = -1;
    private static int auth_USER_ID = -1;
    private static Connection connection = null;
    private static Scanner inputReader = new Scanner(System.in);
    private static Boolean modeDriver = false;

    public static void main(String args[]) throws SQLException {

        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            connection.setAutoCommit(true);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            try{
                run();
            }
            catch (Exception e)
            {
                System.out.println("The program has crashed. Printing stack trace:");
                e.printStackTrace();
            }
            connection.close();
            System.out.println("The database connection has been closed. Good-bye!");
        } catch (Exception e) {
            System.out.println(
                    "Error connecting to database. Printing stack trace: ");
            e.printStackTrace();
        }
    }

    public Olympic(Boolean isDriver)
    {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            connection.setAutoCommit(true);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            if (isDriver)
            {
                inputReader = new Scanner(new File("Driver.txt"));
                modeDriver = true;
            }
            try{
                run();
            }
            catch (Exception e)
            {
                System.out.println("The program has crashed. Printing stack trace:");
                e.printStackTrace();
            }
            connection.close();
            System.out.println("The database connection has been closed. Good-bye!");
        } catch (Exception e) {
            System.out.println(
                    "Error connecting to database. Printing stack trace: ");
            e.printStackTrace();
        }
    }

    public void Close()
    {
        try{
            connection.close();
            System.out.println("The database connection has been closed. Good-bye!");
        }
        catch(Exception e)
        {
            System.out.println(
                    "Error closing connection. Printing stack trace: ");
            e.printStackTrace();
        }
        
    }

    private static void run(){
        System.out.println("Welcome to Landon Higinbotham - LCH43 CS1555 Project");
        boolean running = true;
        while (running)
        {
            System.out.println("\nChoose an option by entering the number to the left of the option.");
            System.out.println("*Reminder: All string inputs are case sensitive.*");
            if (auth_USER_ROLE == -1) //Higher level menu
            {
                System.out.println("1. Login");
                System.out.println("2. Exit");
                String input = inputReader.nextLine();
                if(modeDriver)
                {
                    System.out.println(input);
                }
                if (input.equals("1") || input.equals("1."))
                {
                    System.out.println("Enter username");
                    String username = inputReader.nextLine();
                    if(modeDriver){System.out.println(username);}
                    
                    System.out.println("Enter password");
                    String password = inputReader.nextLine();
                    if(modeDriver){System.out.println(password);}
                    login(username, password);
                }
                else if (input.equals("2") || input.equals("2."))
                {
                    running = exit();
                }
            }
            else //User is signed in
            {
                System.out.println("1. Logout");
                System.out.println("2. Display Sport");
                System.out.println("3. Display Event");
                System.out.println("4. Country Ranking");
                System.out.println("5. Top Atheletes");
                System.out.println("6. Connected Atheletes");
                if (auth_USER_ROLE == 1)
                {
                    System.out.println("7. Create User");
                    System.out.println("8. Drop User");
                    System.out.println("9. Create Event");
                    System.out.println("10. Add Event Outcome");
                }
                else if (auth_USER_ROLE == 2)
                {
                    System.out.println("7. Create Team");
                    System.out.println("8. Register Team");
                    System.out.println("9. Add Participant");
                    System.out.println("10. Add Team Member");
                    System.out.println("11. Drop Team Member");
                }

                String input = inputReader.nextLine();
                if(modeDriver)
                {
                    System.out.println(input);
                }
                if (input.equals("1") || input.equals("1."))
                {
                    logout();
                }
                else if (input.equals("2") || input.equals("2."))
                {
                    displaySport();
                }
                else if (input.equals("3") || input.equals("3."))
                {
                    displayEvent();
                }
                else if (input.equals("4") || input.equals("4."))
                {
                    countryRanking();
                }
                else if (input.equals("5") || input.equals("5."))
                {
                    topkAthletes();
                }
                else if (input.equals("6") || input.equals("6."))
                {
                    connectedAthletes();
                }
                else if (auth_USER_ROLE == 1)
                {
                    if (input.equals("7") || input.equals("7."))
                    {
                        createUser();
                    }
                    else if (input.equals("8") || input.equals("8."))
                    {
                        dropUser();
                    }
                    else if (input.equals("9") || input.equals("9."))
                    {
                        createEvent();
                    }
                    else if (input.equals("10") || input.equals("10."))
                    {
                        addEventOutcome();
                    }
                }
                else if (auth_USER_ROLE == 2)
                {
                    if (input.equals("7") || input.equals("7."))
                    {
                        createTeam();
                    }
                    else if (input.equals("8") || input.equals("8."))
                    {
                        registerTeam();
                    }
                    else if (input.equals("9") || input.equals("9."))
                    {
                        addParticipant();
                    }
                    else if (input.equals("10") || input.equals("10."))
                    {
                        addTeamMember();
                    }
                    else if (input.equals("11") || input.equals("11."))
                    {
                        dropTeamMember();
                    }
                }
            }
        }
    }

    /*Organizer functions*/

    public static void createUser(){
        if (auth_USER_ROLE == 1) //Checks authenticated user for Organizer role
        {
            //Get info for new user
            System.out.println("Enter username");
            String username = inputReader.nextLine();
            if(modeDriver){System.out.println(username);}
            System.out.println("Enter password");
            String password = inputReader.nextLine();
            if(modeDriver){System.out.println(password);}
            System.out.println("Enter role id");
            System.out.println("1 for Organizer. 2 for Coach. 3 for Guest.");
            int roleId = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(roleId);}
            try{
                PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO USER_ACCOUNT (USER_ID, USERNAME, PASSKEY, ROLE_ID, LAST_LOGIN) VALUES (USER_ID_SEQ.NEXTVAL,?,?,?,?)"
                );
                Date curDate = Date.valueOf(LocalDate.now());
                insert.setString(1, username);
                insert.setString(2, password);
                insert.setInt(3, roleId);
                insert.setDate(4, curDate);
                int row = insert.executeUpdate();

                //Get user_id to return back to the person who issued the command.
                int user_ID = -1;
                try{
                    PreparedStatement statement = connection.prepareStatement(
                        "SELECT USER_ID_SEQ.CURRVAL FROM DUAL"
                    );
                    ResultSet rs = statement.executeQuery();
                    if (rs.next())
                    {
                        user_ID = (int) rs.getLong(1);
                    }
                }
                catch (SQLException e)
                {
                    System.out.println("Could not get USER_ID. Printing error trace.");
                    e.printStackTrace();
                }
                if (user_ID != -1)
                {
                    System.out.println("User "+username+" has been created with USER_ID = "+user_ID+"\n");
                }

                if (modeDriver)
                {
                    System.out.println("Showing the last 5 users in USER_ACCOUNT");
                    PreparedStatement st = connection.prepareStatement(
                    "SELECT * FROM (SELECT * FROM USER_ACCOUNT ORDER BY USER_ID DESC) WHERE ROWNUM <= 5"
                    );
                    ResultSet rs = st.executeQuery();
                    if (rs.next())
                    {
                        System.out.println("USER_ID - USERNAME - PASSKEY - ROLE_ID - LAST_LOGIN");
                        System.out.println(rs.getInt("USER_ID")+" - "+rs.getString("USERNAME")+" - "+rs.getString("PASSKEY")+" - "+rs.getInt("ROLE_ID")+" - "+rs.getDate("LAST_LOGIN").toString());
                        while(rs.next())
                        {
                            System.out.println(rs.getInt("USER_ID")+" - "+rs.getString("USERNAME")+" - "+rs.getString("PASSKEY")+" - "+rs.getInt("ROLE_ID")+" - "+rs.getDate("LAST_LOGIN").toString());
                        }
                    }
                }
            }
            catch (SQLException e)
            {
                System.out.println("Could not create user. Printing error trace.");
                e.printStackTrace();
            }
        }
    }

    public static void dropUser(){
        if (auth_USER_ROLE == 1) //Checks authenticated user for Organizer role
        {
            //Get info for new user
            System.out.println("Enter user id");
            int user_id = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(user_id);}
            String username = null;
            try{
                PreparedStatement st = connection.prepareStatement(
                "SELECT * FROM USER_ACCOUNT WHERE USER_ID=?");
                st.setInt(1, user_id);
                ResultSet rs = st.executeQuery();
                if (rs.next()){
                    username = rs.getString("USERNAME");
                }
                else{
                    System.out.println("User not found");
                    return;
                }
            }
            catch (SQLException e){
                System.out.println("Could not find user. Printing error trace.");
                e.printStackTrace();
            }

            System.out.println("Are you sure you want to delete "+username+"?");
            System.out.println("Enter 'Y' to confirm yes.");
            String input = inputReader.nextLine();
            if(modeDriver){System.out.println(input);}
            if (input.toUpperCase().equals("Y") || input.toUpperCase().equals("'Y'"))
            {
                try{
                    PreparedStatement insert = connection.prepareStatement(
                        "DELETE FROM USER_ACCOUNT WHERE USER_ID=?"
                    );
                    insert.setInt(1, user_id);
                    int row = insert.executeUpdate();
                }
                catch (SQLException e)
                {
                    System.out.println("Could not delete user. Printing error trace.");
                    e.printStackTrace();
                }
            }
            try{
                if (modeDriver)
                {
                    System.out.println("Showing the last 5 users in USER_ACCOUNT");
                    PreparedStatement st = connection.prepareStatement(
                    "SELECT * FROM (SELECT * FROM USER_ACCOUNT ORDER BY USER_ID DESC) WHERE ROWNUM <= 5"
                    );
                    ResultSet rs = st.executeQuery();
                    if (rs.next())
                    {
                        System.out.println("USER_ID - USERNAME - PASSKEY - ROLE_ID - LAST_LOGIN");
                        System.out.println(rs.getInt("USER_ID")+" - "+rs.getString("USERNAME")+" - "+rs.getString("PASSKEY")+" - "+rs.getInt("ROLE_ID")+" - "+rs.getDate("LAST_LOGIN").toString());
                        while(rs.next())
                        {
                            System.out.println(rs.getInt("USER_ID")+" - "+rs.getString("USERNAME")+" - "+rs.getString("PASSKEY")+" - "+rs.getInt("ROLE_ID")+" - "+rs.getDate("LAST_LOGIN").toString());
                        }
                    }
                }
            }
            catch(SQLException e)
            {
                System.out.println("Could not show change. Printing error trace.");
                e.printStackTrace();
            }
        }
    }

    public static void createEvent(){
        if (auth_USER_ROLE == 1) //Checks authenticated user for Organizer role
        {
            //Get info for new event
            System.out.println("Enter sport id");
            int sportID = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(sportID);}
            System.out.println("Enter venue id");
            int venueID = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(venueID);}
            System.out.println("Enter gender.");
            System.out.println("Enter 'M' for male and 'F' for female. Do not include the 's.");
            String gender = inputReader.nextLine();
            if(modeDriver){System.out.println(gender);}
            System.out.println("Enter date in 'YYYY-MM-DD' format. E.g 2020-08-21");
            String date = inputReader.nextLine();
            if(modeDriver){System.out.println(date);}
            try{
                PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO EVENT (EVENT_ID, SPORT_ID, VENUE_ID, GENDER, EVENT_TIME) VALUES (EVENT_ID_SEQ.NEXTVAL,?,?,?,?)"
                );
                Date curDate = Date.valueOf(date);
                insert.setInt(1, sportID);
                insert.setInt(2, venueID);
                insert.setString(3, gender);
                insert.setDate(4, curDate);
                int row = insert.executeUpdate();

                //Get user_id to return back to the person who issued the command.
                int event_ID = -1;
                try{
                    PreparedStatement statement = connection.prepareStatement(
                        "SELECT EVENT_ID_SEQ.CURRVAL FROM DUAL"
                    );
                    ResultSet rs = statement.executeQuery();
                    if (rs.next())
                    {
                        event_ID = (int) rs.getLong(1);
                    }
                }
                catch (SQLException e)
                {
                    System.out.println("Could not get event_ID. Printing error trace.");
                    e.printStackTrace();
                }
                if (event_ID != -1)
                {
                    System.out.println("Event "+event_ID+" has been created\n");
                }

                try{
                    if (modeDriver)
                    {
                        System.out.println("Showing the last 5 events in EVENT");
                        PreparedStatement st = connection.prepareStatement(
                        "SELECT * FROM (SELECT * FROM EVENT ORDER BY EVENT_ID DESC) WHERE ROWNUM <= 5"
                        );
                        ResultSet rs = st.executeQuery();
                        if (rs.next())
                        {
                            System.out.println("EVENT_ID - SPORT_ID - VENUE_ID - GENDER - EVENT_TIME");
                            System.out.println(rs.getInt("EVENT_ID")+" - "+rs.getInt("SPORT_ID")+" - "+rs.getInt("VENUE_ID")+" - "+rs.getString("GENDER")+" - "+rs.getDate("EVENT_TIME").toString());
                            while(rs.next())
                            {
                                System.out.println(rs.getInt("EVENT_ID")+" - "+rs.getInt("SPORT_ID")+" - "+rs.getInt("VENUE_ID")+" - "+rs.getString("GENDER")+" - "+rs.getDate("EVENT_TIME").toString());
                            }
                        }
                    }
                }
                catch(SQLException e)
                {
                    System.out.println("Could not show change. Printing error trace.");
                    e.printStackTrace();
                }
            }
            catch (SQLException e)
            {
                System.out.println("Could not create event. Printing error trace.");
                e.printStackTrace();
            }
        }
    }

    public static void addEventOutcome(){
        if (auth_USER_ROLE == 1) //Checks authenticated user for Organizer role
        {
            //Get info
            System.out.println("Enter olympic id");
            int olympicID = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(olympicID);}
            System.out.println("Enter team id");
            int teamID = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(teamID);}
            System.out.println("Enter event id");
            int eventID = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(eventID);}
            System.out.println("Enter participant id");
            int participantID = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(participantID);}
            System.out.println("Enter position");
            int position = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(position);}
            try{
                PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO SCOREBOARD (OLYMPIC_ID, EVENT_ID, TEAM_ID, PARTICIPANT_ID, POSITION, MEDAL_ID) VALUES (?,?,?,?,?,0)"
                );
                insert.setInt(1, olympicID);
                insert.setInt(2, eventID);
                insert.setInt(3, teamID);
                insert.setInt(4, participantID);
                insert.setInt(5, position);
                int row = insert.executeUpdate();

                System.out.println("Event outcome created.\n");
            }
            catch (SQLException e)
            {
                System.out.println("Could not create event. Printing error trace.");
                e.printStackTrace();
            }

            try{
                if (modeDriver)
                {
                    System.out.println("Showing SCOREBOARD rows that have the olympic_id entered for the new outcome");
                    PreparedStatement st = connection.prepareStatement(
                    "SELECT * FROM SCOREBOARD WHERE OLYMPIC_ID=?"
                    );
                    st.setInt(1, olympicID);
                    ResultSet rs = st.executeQuery();
                    if (rs.next())
                    {
                        System.out.println("OLYMPIC_ID - EVENT_ID - TEAM_ID - PARTICIPANT_ID - POSITION - MEDAL_ID");
                        System.out.println(rs.getInt("OLYMPIC_ID")+" - "+rs.getInt("EVENT_ID")+" - "+rs.getInt("TEAM_ID")+" - "+rs.getInt("PARTICIPANT_ID")+" - "+rs.getInt("POSITION")+" - "+rs.getInt("MEDAL_ID"));
                        while(rs.next())
                        {
                            System.out.println(rs.getInt("OLYMPIC_ID")+" - "+rs.getInt("EVENT_ID")+" - "+rs.getInt("TEAM_ID")+" - "+rs.getInt("PARTICIPANT_ID")+" - "+rs.getInt("POSITION")+" - "+rs.getInt("MEDAL_ID"));
                        }
                    }
                }
            }
            catch(SQLException e)
            {
                System.out.println("Could not show change. Printing error trace.");
                e.printStackTrace();
            }
        }
    }

    
    /*Coach functions*/

    private static int retrieveOlympicId(String city, String year)
    {
        int olympic_ID = -1;
        Date startDate = Date.valueOf(year+"-01-01");
        Date endDate = Date.valueOf(year+"-12-31");
        try{
            PreparedStatement st = connection.prepareStatement(
            "SELECT * FROM OLYMPICS WHERE OPENING_DATE>=? AND OPENING_DATE<=? AND HOST_CITY=?");
            st.setDate(1, startDate);
            st.setDate(2, endDate);
            st.setString(3, city);
            ResultSet rs = st.executeQuery();
            if (rs.next()){
                olympic_ID = rs.getInt("OLYMPIC_ID");
            }
            else{
                System.out.println("No such olympics in database for city:"+city+" and year"+year);
            }
        }
        catch (SQLException e){
            System.out.println("Could not retrieve olympics. Printing error trace.");
            e.printStackTrace();
        }
        return olympic_ID;
    }

    public static void createTeam(){
        if (auth_USER_ROLE == 2) //Checks authenticated user for Coach role
        {
            //Get info
            System.out.println("Enter olympic city");
            String olympicCity = inputReader.nextLine();
            if(modeDriver){System.out.println(olympicCity);}
            System.out.println("Enter olympic year");
            String olympicYear = inputReader.nextLine();
            if(modeDriver){System.out.println(olympicYear);}
            int olympicID = retrieveOlympicId(olympicCity, olympicYear);
            System.out.println("Enter sport id");
            int sportID = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(sportID);}
            System.out.println("Enter country id");
            int countryID = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(countryID);}
            System.out.println("Enter team name");
            String teamName = inputReader.nextLine();
            if(modeDriver){System.out.println(teamName);}
            try{
                PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO TEAM (TEAM_ID, OLYMPIC_ID, TEAM_NAME, COUNTRY_ID, SPORT_ID, COACH_ID) VALUES (TEAM_ID_SEQ.NEXTVAL,?,?,?,?,?)"
                );
                insert.setInt(1, olympicID);
                insert.setString(2, teamName);
                insert.setInt(3, countryID);
                insert.setInt(4, sportID);
                insert.setInt(5, auth_USER_ID);
                int row = insert.executeUpdate();
                int teamID = -1;
                try{
                    PreparedStatement statement = connection.prepareStatement(
                        "SELECT TEAM_ID_SEQ.CURRVAL FROM DUAL"
                    );
                    ResultSet rs = statement.executeQuery();
                    if (rs.next())
                    {
                        teamID = (int) rs.getLong(1);
                    }
                }
                catch (SQLException e)
                {
                    System.out.println("Could not get team_ID. Printing error trace.");
                    e.printStackTrace();
                }
                if (teamID != -1)
                {
                    System.out.println("Team with TEAM_ID "+teamID+" created.\n");
                }
                try{
                    if (modeDriver)
                    {
                        System.out.println("Showing the last 5 teams in TEAM");
                        PreparedStatement st = connection.prepareStatement(
                        "SELECT * FROM (SELECT * FROM TEAM ORDER BY TEAM_ID DESC) WHERE ROWNUM <= 5"
                        );
                        ResultSet rs = st.executeQuery();
                        if (rs.next())
                        {
                            System.out.println("TEAM_ID - OLYMPIC_ID - TEAM_NAME - COUNTRY_ID - SPORT_ID - COACH_ID");
                            System.out.println(rs.getInt("TEAM_ID")+" - "+rs.getInt("OLYMPIC_ID")+" - "+rs.getString("TEAM_NAME")+" - "+rs.getInt("COUNTRY_ID")+" - "+rs.getInt("SPORT_ID")+" - "+rs.getInt("COACH_ID"));
                            while(rs.next())
                            {
                                System.out.println(rs.getInt("TEAM_ID")+" - "+rs.getInt("OLYMPIC_ID")+" - "+rs.getString("TEAM_NAME")+" - "+rs.getInt("COUNTRY_ID")+" - "+rs.getInt("SPORT_ID")+" - "+rs.getInt("COACH_ID"));
                            }
                        }
                    }
                }
                catch(SQLException e)
                {
                    System.out.println("Could not show change. Printing error trace.");
                    e.printStackTrace();
                }
            }
            catch (SQLException e)
            {
                System.out.println("Could not create event. Printing error trace.");
                e.printStackTrace();
            }
        }
    }
    
    public static void registerTeam(){
        if (auth_USER_ROLE == 2) //Checks authenticated user for Coach role
        {
            //Get info
            System.out.println("Enter team id");
            int teamID = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(teamID);}
            System.out.println("Enter event id");
            int eventID = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(eventID);}
            try{
                PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO EVENT_PARTICIPATION (EVENT_ID, TEAM_ID, STATUS) VALUES (?,?,'e')"
                );
                insert.setInt(1, eventID);
                insert.setInt(2, teamID);
                int row = insert.executeUpdate();
                System.out.println("TEAM_ID "+teamID+" registered for EVENT_ID "+eventID+" \n");
            }
            catch (SQLException e)
            {
                System.out.println("Could not register team. Printing error trace.");
                e.printStackTrace();
            }
            try{
                if (modeDriver)
                {
                    System.out.println("Showing the rows in EVENT_PARTICIPATION for the given EVENT_ID");
                    PreparedStatement st = connection.prepareStatement(
                    "SELECT * FROM EVENT_PARTICIPATION WHERE EVENT_ID=?"
                    );
                    st.setInt(1, eventID);
                    ResultSet rs = st.executeQuery();
                    if (rs.next())
                    {
                        System.out.println("EVENT_ID - TEAM_ID - STATUS");
                        System.out.println(rs.getInt("EVENT_ID")+" - "+rs.getInt("TEAM_ID")+" - "+rs.getString("STATUS"));
                        while(rs.next())
                        {
                            System.out.println(rs.getInt("EVENT_ID")+" - "+rs.getInt("TEAM_ID")+" - "+rs.getString("STATUS"));
                        }
                    }
                }
            }
            catch(SQLException e)
            {
                System.out.println("Could not show change. Printing error trace.");
                e.printStackTrace();
            }
        }
    }

    public static void addParticipant(){
        if (auth_USER_ROLE == 2) //Checks authenticated user for Coach role
        {
            //Get info
            System.out.println("Enter first name");
            String firstName = inputReader.nextLine();
            if(modeDriver){System.out.println(firstName);}
            System.out.println("Enter last name");
            String lastName = inputReader.nextLine();
            if(modeDriver){System.out.println(lastName);}
            System.out.println("Enter nationality");
            String nationality = inputReader.nextLine();
            if(modeDriver){System.out.println(nationality);}
            System.out.println("Enter birth place");
            String birthPlace = inputReader.nextLine();
            if(modeDriver){System.out.println(birthPlace);}
            System.out.println("Enter date of birth in 'YYYY-MM-DD' format. E.g 2020-08-21");
            String date = inputReader.nextLine();
            if(modeDriver){System.out.println(date);}
            try{
                PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO PARTICIPANT (PARTICIPANT_ID, FNAME, LNAME, NATIONALITY, BIRTH_PLACE, DOB) VALUES (PARTICIPANT_ID_SEQ.NEXTVAL,?,?,?,?,?)"
                );
                Date newDate = Date.valueOf(date);
                insert.setString(1, firstName);
                insert.setString(2, lastName);
                insert.setString(3, nationality);
                insert.setString(4, birthPlace);
                insert.setDate(5, newDate);
                int row = insert.executeUpdate();
                int participantID = -1;
                try{
                    PreparedStatement statement = connection.prepareStatement(
                        "SELECT PARTICIPANT_ID_SEQ.CURRVAL FROM DUAL"
                    );
                    ResultSet rs = statement.executeQuery();
                    if (rs.next())
                    {
                        participantID = (int) rs.getLong(1);
                    }
                }
                catch (SQLException e)
                {
                    System.out.println("Could not get participant_ID. Printing error trace.");
                    e.printStackTrace();
                }
                if (participantID != -1)
                {
                    System.out.println("Participant with PARTICIPANT_ID "+participantID+" created.\n");
                }
                try{
                    if (modeDriver)
                    {
                        System.out.println("Showing the last 5 participants in PARTICIPANT");
                        PreparedStatement st = connection.prepareStatement(
                        "SELECT * FROM (SELECT * FROM PARTICIPANT ORDER BY PARTICIPANT_ID DESC) WHERE ROWNUM <= 5"
                        );
                        ResultSet rs = st.executeQuery();
                        if (rs.next())
                        {
                            System.out.println("PARTICIPANT_ID - FNAME - LNAME - NATIONALITY - BIRTH_PLACE - DOB");
                            System.out.println(rs.getInt("PARTICIPANT_ID")+" - "+rs.getString("FNAME")+" - "+rs.getString("LNAME")+" - "+rs.getString("NATIONALITY")+" - "+rs.getString("BIRTH_PLACE")+" - "+rs.getDate("DOB").toString());
                            while(rs.next())
                            {
                                System.out.println(rs.getInt("PARTICIPANT_ID")+" - "+rs.getString("FNAME")+" - "+rs.getString("LNAME")+" - "+rs.getString("NATIONALITY")+" - "+rs.getString("BIRTH_PLACE")+" - "+rs.getDate("DOB").toString());
                            }
                        }
                    }
                }
                catch(SQLException e)
                {
                    System.out.println("Could not show change. Printing error trace.");
                    e.printStackTrace();
                }
            }
            catch (SQLException e)
            {
                System.out.println("Could not add participant. Printing error trace.");
                e.printStackTrace();
            }
        }
    }

    public static void addTeamMember(){
        if (auth_USER_ROLE == 2) //Checks authenticated user for Coach role
        {
            //Get info
            System.out.println("Enter team id");
            int teamID = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(teamID);}
            System.out.println("Enter participant id");
            int participantID = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(participantID);}
            try{
                PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO TEAM_MEMBER (TEAM_ID, PARTICIPANT_ID) VALUES (?,?)"
                );
                insert.setInt(1, teamID);
                insert.setInt(2, participantID);
                int row = insert.executeUpdate();
                System.out.println("PARTICIPANT_ID "+participantID+" added to TEAM_ID "+teamID+" \n");
            }
            catch (SQLException e)
            {
                System.out.println("Could not add team member. Printing error trace.");
                e.printStackTrace();
            }
            try{
                if (modeDriver)
                {
                    System.out.println("Showing the team members for given TEAM_ID");
                    PreparedStatement st = connection.prepareStatement(
                    "SELECT * FROM TEAM_MEMBER WHERE TEAM_ID = ?"
                    );
                    st.setInt(1, teamID);
                    ResultSet rs = st.executeQuery();
                    if (rs.next())
                    {
                        System.out.println("TEAM_ID - PARTICIPANT_ID");
                        System.out.println(rs.getInt("TEAM_ID")+" - "+rs.getInt("PARTICIPANT_ID"));
                        while(rs.next())
                        {
                            System.out.println(rs.getInt("TEAM_ID")+" - "+rs.getInt("PARTICIPANT_ID"));
                        }
                    }
                }
            }
            catch(SQLException e)
            {
                System.out.println("Could not show change. Printing error trace.");
                e.printStackTrace();
            }
        }
    }

    public static void dropTeamMember(){
        if (auth_USER_ROLE == 2) //Checks authenticated user for Coach role
        {
            //Get info
            System.out.println("Enter participant id");
            int participantID = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(participantID);}
            try{
                PreparedStatement insert = connection.prepareStatement(
                    "DELETE FROM PARTICIPANT WHERE PARTICIPANT_ID=?"
                );
                insert.setInt(1, participantID);
                int row = insert.executeUpdate();
                System.out.println("PARTICIPANT_ID "+participantID+" removed \n");
            }
            catch (SQLException e)
            {
                System.out.println("Could not drop team member. Printing error trace.");
                e.printStackTrace();
            }
            try{
                if (modeDriver)
                {
                    System.out.println("Showing all event participation");
                    PreparedStatement st = connection.prepareStatement(
                    "SELECT * FROM EVENT_PARTICIPATION"
                    );
                    ResultSet rs = st.executeQuery();
                    if (rs.next())
                    {
                        System.out.println("EVENT_ID - TEAM_ID - STATUS");
                        System.out.println(rs.getInt("EVENT_ID")+" - "+rs.getInt("TEAM_ID")+" - "+rs.getString("STATUS"));
                        while(rs.next())
                        {
                            System.out.println(rs.getInt("EVENT_ID")+" - "+rs.getInt("TEAM_ID")+" - "+rs.getString("STATUS"));
                        }
                    }

                    System.out.println("Showing all team members");

                    st = connection.prepareStatement(
                    "SELECT * FROM TEAM_MEMBER"
                    );
                    rs = st.executeQuery();
                    if (rs.next())
                    {
                        System.out.println("TEAM_ID - PARTICIPANT_ID");
                        System.out.println(rs.getInt("TEAM_ID")+" - "+rs.getInt("PARTICIPANT_ID"));
                        while(rs.next())
                        {
                            System.out.println(rs.getInt("TEAM_ID")+" - "+rs.getInt("PARTICIPANT_ID"));
                        }
                    }
                }
            }
            catch(SQLException e)
            {
                System.out.println("Could not show change. Printing error trace.");
                e.printStackTrace();
            }
        }
    }

    
    /*All functions*/

    public static void login(String username, String password){
        try{
            PreparedStatement st = connection.prepareStatement(
            "SELECT * FROM USER_ACCOUNT WHERE USERNAME=? AND PASSKEY=?");
            st.setString(1, username);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            if (rs.next()){
                auth_USER_ID = rs.getInt("USER_ID");
                auth_USER_ROLE = rs.getInt("ROLE_ID");
                System.out.println("Login successful. Welcome "+username+"!");
            }
            else{
                System.out.println("Username / password not recognized");
            }
        }
        catch (SQLException e){
            System.out.println("Could not log in. Printing error trace.");
            e.printStackTrace();
        }
    }

    /*
    SELECT SPORT_ID, SPORT_NAME, DESCRIPTION, DOB AS SPORT_DOB FROM SPORT WHERE SPORT_NAME='Basketball';

    SELECT EVENT_ID, EVENT_TIME, GENDER, FNAME, LNAME, NATIONALITY, MEDAL_ID
    FROM PARTICIPANT P JOIN (SELECT SC.EVENT_ID, EVENT_TIME, GENDER, PARTICIPANT_ID, MEDAL_ID FROM SCOREBOARD SC JOIN (SELECT EVENT_ID, EVENT_TIME, GENDER, E.SPORT_ID FROM EVENT E JOIN (SELECT SPORT_ID FROM SPORT WHERE SPORT_NAME=?) SP ON E.SPORT_ID = SP.SPORT_ID) S ON SC.EVENT_ID = S.EVENT_ID) S ON P.PARTICIPANT_ID = S.PARTICIPANT_ID
    WHERE MEDAL_ID > 0
    ORDER BY EVENT_TIME ASC, MEDAL_ID ASC
    ;
    */

    public static void displaySport(){
        try{
            System.out.println("Enter sport name");
            String sportName = inputReader.nextLine();
            if(modeDriver){System.out.println(sportName);}
            //Display sport info
            PreparedStatement st = connection.prepareStatement(
            "SELECT SPORT_ID, SPORT_NAME, DESCRIPTION, DOB AS SPORT_DOB FROM SPORT WHERE SPORT_NAME=?"
            );
            st.setString(1, sportName);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                System.out.println(sportName+": "+rs.getString("DESCRIPTION"));
                System.out.println(sportName+" was added on "+rs.getDate("SPORT_DOB").toString()+".");
            }
            //Display winner info
            st = connection.prepareStatement(
            "SELECT EVENT_ID, EVENT_TIME, GENDER, FNAME, LNAME, NATIONALITY, MEDAL_ID FROM PARTICIPANT P JOIN (SELECT SC.EVENT_ID, EVENT_TIME, GENDER, PARTICIPANT_ID, MEDAL_ID FROM SCOREBOARD SC JOIN (SELECT EVENT_ID, EVENT_TIME, GENDER, E.SPORT_ID FROM EVENT E JOIN (SELECT SPORT_ID FROM SPORT WHERE SPORT_NAME=?) SP ON E.SPORT_ID = SP.SPORT_ID) S ON SC.EVENT_ID = S.EVENT_ID) S ON P.PARTICIPANT_ID = S.PARTICIPANT_ID WHERE MEDAL_ID > 0 ORDER BY EVENT_TIME ASC, MEDAL_ID ASC"
            );
            st.setString(1, sportName);
            rs = st.executeQuery();
            if (rs.next()){
                System.out.println(rs.getString("FNAME")+" "+rs.getString("LNAME")+" - "+rs.getString("NATIONALITY")+" - Event: "+rs.getInt("EVENT_ID")+" - Gender: "+rs.getString("GENDER")+" - Medal: "+rs.getInt("MEDAL_ID"));
                while(rs.next())
                {
                    System.out.println(rs.getString("FNAME")+" "+rs.getString("LNAME")+" - "+rs.getString("NATIONALITY")+" - Event: "+rs.getInt("EVENT_ID")+" - Gender: "+rs.getString("GENDER")+" - Medal: "+rs.getInt("MEDAL_ID"));
                }
            }

        }
        catch (SQLException e){
            System.out.println("Could not display sport. Printing error trace.");
            e.printStackTrace();
        }
    }

    /*

    SELECT OLYMPIC_NUM, EVENT_ID, FNAME, LNAME, POSITION, MEDAL_ID
    FROM PARTICIPANT P JOIN (SELECT OLYMPIC_NUM, EVENT_ID, PARTICIPANT_ID, POSITION, MEDAL_ID FROM OLYMPICS O JOIN (SELECT OLYMPIC_ID, EVENT_ID, PARTICIPANT_ID, POSITION, MEDAL_ID FROM SCOREBOARD WHERE OLYMPIC_ID =? AND EVENT_ID =?) E ON O.OLYMPIC_ID = E.OLYMPIC_ID) S ON P.PARTICIPANT_ID = S.PARTICIPANT_ID
    ORDER BY POSITION ASC
    ;*/

    public static void displayEvent(){
        try{
            //Get info
            System.out.println("Enter olympic city");
            String olympicCity = inputReader.nextLine();
            if(modeDriver){System.out.println(olympicCity);}
            System.out.println("Enter olympic year");
            String olympicYear = inputReader.nextLine();
            if(modeDriver){System.out.println(olympicYear);}
            int olympicID = retrieveOlympicId(olympicCity, olympicYear);
            System.out.println("Enter event id");
            int eventID = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(eventID);}

            PreparedStatement st = connection.prepareStatement(
            "SELECT OLYMPIC_NUM, EVENT_ID, FNAME, LNAME, POSITION, MEDAL_ID FROM PARTICIPANT P JOIN (SELECT OLYMPIC_NUM, EVENT_ID, PARTICIPANT_ID, POSITION, MEDAL_ID FROM OLYMPICS O JOIN (SELECT OLYMPIC_ID, EVENT_ID, PARTICIPANT_ID, POSITION, MEDAL_ID FROM SCOREBOARD WHERE OLYMPIC_ID =? AND EVENT_ID =?) E ON O.OLYMPIC_ID = E.OLYMPIC_ID) S ON P.PARTICIPANT_ID = S.PARTICIPANT_ID ORDER BY POSITION ASC"
            );
            st.setInt(1, olympicID);
            st.setInt(2, eventID);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                System.out.println("Olympic "+rs.getString("OLYMPIC_NUM")+" - "+rs.getString("FNAME")+" "+rs.getString("LNAME")+" - Event: "+rs.getInt("EVENT_ID")+" - Position: "+rs.getInt("POSITION")+" - Medal: "+rs.getInt("MEDAL_ID"));
                while(rs.next())
                {
                    System.out.println("Olympic "+rs.getString("OLYMPIC_NUM")+" - "+rs.getString("FNAME")+" "+rs.getString("LNAME")+" - Event: "+rs.getInt("EVENT_ID")+" - Position: "+rs.getInt("POSITION")+" - Medal: "+rs.getInt("MEDAL_ID"));
                }
            }
        }
        catch (SQLException e){
            System.out.println("Could not display event. Printing error trace.");
            e.printStackTrace();
        }
    }

    /*
    SELECT STARTED, COUNTRY, GOLD, SILVER, BRONZE, POINTS
    FROM COUNTRY C JOIN
    (
    SELECT STARTED, S.COUNTRY_ID, GOLD, SILVER, BRONZE, POINTS
    FROM 
    (
    SELECT MIN(OPENING_DATE) AS STARTED, COUNTRY_ID
    FROM TEAM T JOIN
    (
    SELECT OPENING_DATE, TEAM_ID
    FROM OLYMPICS O JOIN SCOREBOARD S ON O.OLYMPIC_ID = S.OLYMPIC_ID
    ) S ON T.TEAM_ID = S.TEAM_ID
    GROUP BY COUNTRY_ID
    )
    O JOIN
    (
    SELECT COUNTRY_ID, SUM(GOLD) AS GOLD, SUM(SILVER) AS SILVER, SUM(BRONZE) AS BRONZE, SUM(POINTS) AS POINTS
    FROM TEAM T JOIN
    (
    SELECT TEAM_ID, SUM(CASE WHEN M.MEDAL_ID=1 THEN 1 ELSE 0 END) AS GOLD ,SUM(CASE WHEN M.MEDAL_ID=2 THEN 1 ELSE 0 END) AS SILVER ,SUM(CASE WHEN M.MEDAL_ID=3 THEN 1 ELSE 0 END) AS BRONZE, SUM(POINTS) AS POINTS
    FROM SCOREBOARD S JOIN MEDAL M ON S.MEDAL_ID = M.MEDAL_ID
    WHERE OLYMPIC_ID = 1
    GROUP BY TEAM_ID
    ) S ON T.TEAM_ID = S.TEAM_ID
    GROUP BY COUNTRY_ID, OLYMPIC_ID
    ) S ON O.COUNTRY_ID = S.COUNTRY_ID
    GROUP BY S.COUNTRY_ID, STARTED, GOLD, SILVER, BRONZE, POINTS
    ORDER BY POINTS DESC
    ) S ON C.COUNTRY_ID = S.COUNTRY_ID
    ;
    */

    public static void countryRanking(){
        try{
            //Get info
            System.out.println("Enter olympic id");
            int olympicID = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(olympicID);}

            PreparedStatement st = connection.prepareStatement(
            "SELECT STARTED, COUNTRY, GOLD, SILVER, BRONZE, POINTS "+
            "FROM COUNTRY C JOIN "+
            "( "+
            "SELECT STARTED, S.COUNTRY_ID, GOLD, SILVER, BRONZE, POINTS "+
            "FROM "+
            "("+
            "SELECT MIN(OPENING_DATE) AS STARTED, COUNTRY_ID "+
            "FROM TEAM T JOIN "+
            "("+
            "SELECT OPENING_DATE, TEAM_ID "+
            "FROM OLYMPICS O JOIN SCOREBOARD S ON O.OLYMPIC_ID = S.OLYMPIC_ID "+
            ") S ON T.TEAM_ID = S.TEAM_ID "+
            "GROUP BY COUNTRY_ID "+
            ") "+
            "O JOIN "+
            "("+
            "SELECT COUNTRY_ID, SUM(GOLD) AS GOLD, SUM(SILVER) AS SILVER, SUM(BRONZE) AS BRONZE, SUM(POINTS) AS POINTS "+
            "FROM TEAM T JOIN "+
            "("+
            "SELECT TEAM_ID, SUM(CASE WHEN M.MEDAL_ID=1 THEN 1 ELSE 0 END) AS GOLD ,SUM(CASE WHEN M.MEDAL_ID=2 THEN 1 ELSE 0 END) AS SILVER ,SUM(CASE WHEN M.MEDAL_ID=3 THEN 1 ELSE 0 END) AS BRONZE, SUM(POINTS) AS POINTS "+
            "FROM SCOREBOARD S JOIN MEDAL M ON S.MEDAL_ID = M.MEDAL_ID "+
            "WHERE OLYMPIC_ID = ?"+
            "GROUP BY TEAM_ID "+
            ") S ON T.TEAM_ID = S.TEAM_ID "+
            "GROUP BY COUNTRY_ID, OLYMPIC_ID "+
            ") S ON O.COUNTRY_ID = S.COUNTRY_ID "+
            "GROUP BY S.COUNTRY_ID, STARTED, GOLD, SILVER, BRONZE, POINTS "+
            "ORDER BY POINTS DESC "+
            ") S ON C.COUNTRY_ID = S.COUNTRY_ID"
            );
            st.setInt(1, olympicID);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                System.out.println("Started: "+rs.getDate("STARTED").toString()+" - "+rs.getString("COUNTRY")+" - Golds: "+rs.getInt("GOLD")+" - Silvers: "+rs.getInt("SILVER")+" - Bronze: "+rs.getInt("BRONZE")+" - Points: "+rs.getInt("POINTS"));
                while(rs.next())
                {
                    System.out.println("Started: "+rs.getDate("STARTED").toString()+" - "+rs.getString("COUNTRY")+" - Golds: "+rs.getInt("GOLD")+" - Silvers: "+rs.getInt("SILVER")+" - Bronze: "+rs.getInt("BRONZE")+" - Points: "+rs.getInt("POINTS"));
                }
            }
        }
        catch (SQLException e){
            System.out.println("Could not display country ranking. Printing error trace.");
            e.printStackTrace();
        }
    }
    /*
    SELECT *
    FROM
    (
    SELECT FNAME, LNAME, SUM(CASE WHEN MEDAL_ID=1 THEN 1 ELSE 0 END) AS GOLD ,SUM(CASE WHEN MEDAL_ID=2 THEN 1 ELSE 0 END) AS SILVER ,SUM(CASE WHEN MEDAL_ID=3 THEN 1 ELSE 0 END) AS BRONZE, SUM(POINTS) AS POINTS
    FROM PARTICIPANT P JOIN
    (
    SELECT PARTICIPANT_ID, S.MEDAL_ID, POINTS
    FROM MEDAL M JOIN
    (
    SELECT PARTICIPANT_ID, MEDAL_ID
    FROM SCOREBOARD
    WHERE OLYMPIC_ID = 1
    ) S ON M.MEDAL_ID = S.MEDAL_ID
    ) S ON P.PARTICIPANT_ID = S.PARTICIPANT_ID
    GROUP BY FNAME, LNAME
    ORDER BY POINTS DESC, LNAME ASC
    )
    WHERE ROWNUM <= 5
    ;
    */

    public static void topkAthletes(){
        try{
            //Get info
            System.out.println("Enter olympic id");
            int olympicID = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(olympicID);}
            System.out.println("Enter athelete count");
            int numAth = Integer.parseInt(inputReader.nextLine());
            if(modeDriver){System.out.println(numAth);}

            PreparedStatement st = connection.prepareStatement(
            "SELECT * "+
            "FROM "+
            "( "+
            "SELECT FNAME, LNAME, SUM(CASE WHEN MEDAL_ID=1 THEN 1 ELSE 0 END) AS GOLD ,SUM(CASE WHEN MEDAL_ID=2 THEN 1 ELSE 0 END) AS SILVER ,SUM(CASE WHEN MEDAL_ID=3 THEN 1 ELSE 0 END) AS BRONZE, SUM(POINTS) AS POINTS "+
            "FROM PARTICIPANT P JOIN "+
            "("+
            "SELECT PARTICIPANT_ID, S.MEDAL_ID, POINTS "+
            "FROM MEDAL M JOIN "+
            "("+
            "SELECT PARTICIPANT_ID, MEDAL_ID "+
            "FROM SCOREBOARD "+
            "WHERE OLYMPIC_ID=? "+
            ") S ON M.MEDAL_ID = S.MEDAL_ID "+
            ") S ON P.PARTICIPANT_ID = S.PARTICIPANT_ID "+
            "GROUP BY FNAME, LNAME "+
            "ORDER BY POINTS DESC, LNAME ASC"+
            ") "+
            "WHERE ROWNUM <=? "
            );
            st.setInt(1, olympicID);
            st.setInt(2, numAth);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                System.out.println(rs.getString("FNAME")+" "+rs.getString("LNAME")+" - Golds: "+rs.getInt("GOLD")+" - Silvers: "+rs.getInt("SILVER")+" - Bronze: "+rs.getInt("BRONZE")+" - Points: "+rs.getInt("POINTS"));
                while(rs.next())
                {
                    System.out.println(rs.getString("FNAME")+" "+rs.getString("LNAME")+" - Golds: "+rs.getInt("GOLD")+" - Silvers: "+rs.getInt("SILVER")+" - Bronze: "+rs.getInt("BRONZE")+" - Points: "+rs.getInt("POINTS"));
                }
            }
        }
        catch (SQLException e){
            System.out.println("Could not display top atheletes. Printing error trace.");
            e.printStackTrace();
        }
    }

    public static void connectedAthletes(){
        System.out.println("This function has not yet been implemented.");
    }

    public static void logout(){
        try{
            PreparedStatement st = connection.prepareStatement(
            "SELECT * FROM USER_ACCOUNT WHERE USER_ID=?");
            st.setString(1, auth_USER_ID+"");
            ResultSet rs = st.executeQuery();
            if (rs.next()){
                //Set last_login
                PreparedStatement update = connection.prepareStatement(
                    "UPDATE USER_ACCOUNT SET LAST_LOGIN=? WHERE USER_ID=?"
                );
                Date curDate = Date.valueOf(LocalDate.now());
                update.setDate(1, curDate);
                update.setInt(2, auth_USER_ID);
                int row = update.executeUpdate();
            }
            //Resets saved authentication
            auth_USER_ID = -1;
            auth_USER_ROLE = -1;
        }
        catch (SQLException e){
            System.out.println("Could not log out. Printing error trace.");
            e.printStackTrace();
        }
    }

    public static boolean exit(){
        try{
            logout();
            connection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }


}
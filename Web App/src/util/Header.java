package util;

import java.io.PrintWriter;

public class Header {

  public static String printHeader(int id, String username, PrintWriter out) {
    String header = null;
    try {
      Env env = new Env();
      header = "<div class=\"row header\">\n"
          + "<div class=\"header-logo\">\n"
          + "<div class=\"logo-text\">VROOOM!</div>\n"
          + "<div class=\"logo-subtext\">A Solution for Your Transportation</div>\n"
          + "</div>\n"
          + "<div class=\"header-user\">\n"
          + "<div class=\"header-username\">Hello! <span class=\"username\">" + username
          + "</span></div>\n"
          + "<div class=\"header-logout\"><a href=\"http://localhost:" + env.getIdentityPort()
          + "\\logout\">Logout!</a></div>\n"
          + "</div>\n"
          + "</div>";
    } catch (Exception e) {
      e.printStackTrace();
    }
    return header;
  }

  public static String printNavbar(int id, int page, PrintWriter out) {
    String nav = null;
    if (page == 0) {
      nav = "<div class=\"nav\">\n"
          + "              <a href=\"order-selectdestination.jsp?id=" + id
          + "\"><div class=\"nav-item active\">ORDER</div></a>\n"
          + "<a href=\"history-orderhistory.jsp?id=" + id
          + "\"><div class=\"nav-item\">HISTORY</div></a>\n"
          + "<a href=\"profile.jsp?id=" + id
          + "\"><div class=\"nav-item\">MY PROFILE</div></a></div>";
    } else if (page == 1) {
      nav = "<div class=\"nav\">\n"
          + "              <a href=\"order-selectdestination.jsp?id=" + id
          + "\"><div class=\"nav-item\">ORDER</div></a>\n"
          + "<a href=\"history-orderhistory.jsp?id=" + id
          + "\"><div class=\"nav-item active\">HISTORY</div></a>\n"
          + "<a href=\"profile.jsp?id=" + id
          + "\"><div class=\"nav-item\">MY PROFILE</div></a></div>";
    } else if (page == 2) {
      nav = "<div class=\"nav\">\n"
          + "              <a href=\"order-selectdestination.jsp?id=" + id
          + "\"><div class=\"nav-item\">ORDER</div></a>\n"
          + "<a href=\"history-orderhistory.jsp?id=" + id
          + "\"><div class=\"nav-item\">HISTORY</div></a>\n"
          + "<a href=\"profile.jsp?id=" + id
          + "\"><div class=\"nav-item active\">MY PROFILE</div></a></div>";
    }
    return nav;
  }

  public static String printProfile(boolean isDriver, String profilePic, String type) {
    StringBuilder out = new StringBuilder("<img ");

    if (!profilePic.equals(".")) {
      out.append("src=\"data:image/jpeg;base64,").append(profilePic).append("\" ");
    }
    out.append("class=\""+type);

    if (isDriver) {
      out.append("driver\"");
    }
    else{
      out.append("user\"");
    }
    out.append("></img>");

    return out.toString();
  }
}

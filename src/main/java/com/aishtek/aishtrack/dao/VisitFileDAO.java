package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.aishtek.aishtrack.beans.VisitFile;

public class VisitFileDAO extends BaseDAO {

  public static int create(Connection connection, VisitFile visitFile) throws SQLException {
    PreparedStatement preparedStatement =
        connection.prepareStatement("insert into visit_files (visit_id, file_id) values(?, ?)",
            PreparedStatement.RETURN_GENERATED_KEYS);

    preparedStatement.setInt(1, visitFile.getVisitId());
    preparedStatement.setInt(2, visitFile.getFileId());
    preparedStatement.executeUpdate();

    ResultSet result = preparedStatement.getGeneratedKeys();
    if (result.next()) {
      return result.getInt(1);
    } else {
      throw new SQLException("Visit File ID not generted");
    }
  }
}

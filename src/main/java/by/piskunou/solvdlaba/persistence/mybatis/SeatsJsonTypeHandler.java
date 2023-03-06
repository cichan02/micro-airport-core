package by.piskunou.solvdlaba.persistence.mybatis;

import by.piskunou.solvdlaba.domain.Seat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@Component
@MappedTypes(List.class)
@RequiredArgsConstructor
public class SeatsJsonTypeHandler extends BaseTypeHandler<List<Seat>> {

    private final Gson gson;

    @Override
    @SneakyThrows
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<Seat> seats, JdbcType jdbcType) {
        Type listType = new TypeToken<List<Seat>>(){}.getType();
        String json = gson.toJson(seats, listType);

        preparedStatement.setObject(i, json);
    }

    @Override
    @SneakyThrows
    public List<Seat> getNullableResult(ResultSet resultSet, String s) {
        if(resultSet.getString(s) != null) {
            Type listType = new TypeToken<List<Seat>>() {}.getType();
            String json = resultSet.getString(s);
            return gson.fromJson(json, listType);
        }
        return null;
    }

    @Override
    @SneakyThrows
    public List<Seat> getNullableResult(ResultSet resultSet, int i) {
        if(resultSet.getString(i) != null) {
            Type listType = new TypeToken<List<Seat>>() {}.getType();
            String json = resultSet.getString(i);
            return gson.fromJson(json, listType);
        }
        return null;
    }

    @Override
    @SneakyThrows
    public List<Seat> getNullableResult(CallableStatement callableStatement, int i) {
        if(callableStatement.getString(i) != null) {
            Type listType = new TypeToken<List<Seat>>() {}.getType();
            String json = callableStatement.getString(i);
            return gson.fromJson(json, listType);
        }
        return null;
    }
}

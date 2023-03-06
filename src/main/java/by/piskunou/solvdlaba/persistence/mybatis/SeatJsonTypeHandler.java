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

@Component
@MappedTypes(Seat.class)
@RequiredArgsConstructor
public class SeatJsonTypeHandler extends BaseTypeHandler<Seat> {

    private final Gson gson;

    @Override
    @SneakyThrows
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Seat seat, JdbcType jdbcType) {
        Type seatType = new TypeToken<Seat>(){}.getType();
        String json = gson.toJson(seat, seatType);

        preparedStatement.setObject(i, json);
    }

    @Override
    @SneakyThrows
    public Seat getNullableResult(ResultSet resultSet, String s) {
        if(resultSet.getString(s) != null) {
            Type seatType = new TypeToken<Seat>(){}.getType();
            String json = resultSet.getString(s);
            return gson.fromJson(json, seatType);
        }
        return null;
    }

    @Override
    @SneakyThrows
    public Seat getNullableResult(ResultSet resultSet, int i) {
        if(resultSet.getString(i) != null) {
            Type seatType = new TypeToken<Seat>(){}.getType();
            String json = resultSet.getString(i);
            return gson.fromJson(json, seatType);
        }
        return null;
    }

    @Override
    @SneakyThrows
    public Seat getNullableResult(CallableStatement callableStatement, int i) {
        if(callableStatement.getString(i) != null) {
            Type seatType = new TypeToken<Seat>(){}.getType();
            String json = callableStatement.getString(i);
            return gson.fromJson(json, seatType);
        }
        return null;
    }
}

package core.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author valor
 */
public interface TestMapper {

    void insert(@Param("name") String name);

}

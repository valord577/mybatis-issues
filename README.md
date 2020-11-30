# mybatis-issues

----------------------

## run

    java \
      -Dfile.encoding=UTF-8 \
      -Djava.complier=NONE \
      -jar ${jarName} --active=starter
      
## shutdown

    // see `boot.verticle.SysVerticle` for detail
    curl --request GET \
      --url 'http://[::1]:60000/close'

## mysql ddl

    create table zzzz_table_test
    (
      id int auto_increment
          primary key,
      name varchar(4) not null
    );

## something important

    // main class
    boot.MainApplication
    
    // restful http
    api.controller.*
    
    // controller register
    boot.verticle.*
    
    // configuration file (at `resources`)

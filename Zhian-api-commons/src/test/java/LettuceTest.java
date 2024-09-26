import org.example.Main;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes =  Main.class)
public class LettuceTest {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Test
    public void t1(){
        String key = "key1";
        System.out.println("插入数据到redis");
        redisTemplate.opsForValue().set(key,"value1");
        System.out.println("开始获取数据 ");
        Object value = redisTemplate.opsForValue().get(key);
        System.out.println("从redis中获取到值为 "+value);
        Boolean delete = redisTemplate.delete(key);
        System.out.println("删除redis中值 "+delete);
    }
}

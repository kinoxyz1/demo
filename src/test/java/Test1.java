import com.kino.study.design.factory.FactoryUtils;
import com.kino.study.design.factory.TableSource;
import com.kino.study.design.factory.TableSourceFactory;

/**
 * @author kino
 * @date 2024/5/11 18:20
 */
public class Test1 {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        TableSourceFactory factory = FactoryUtils.createTableSource("kafka");
        TableSource tableSource = factory.createTableSource();
        tableSource.open();
        tableSource.run();
        tableSource.close();
    }
}

package lv.roma.study;

import org.jooby.Jooby;
import org.jooby.hbs.Hbs;
import org.jooby.json.Jackson;

public class App extends Jooby {

    {
        assets("/css/**");
        assets("/images/**");
        assets("/js/**");

        /* Template engine: */
        use(new Hbs());

        /* JSON: */
        use(new Jackson());

        /*Controllers*/
        use(IndexController.class);
    }

    public static void main(final String[] args) {
        run(App::new, args);
    }

}

package controllers.data;

import models.RodzajDecyzji;
import play.data.validation.Constraints;

public class DecyzjaData {

    public DecyzjaData() {}

    @Constraints.Required
    private String rodzajDecyzji;

    @Constraints.Required
    private String uzasadnienieDecyzji;

    public String getRodzajDecyzji() {
        return rodzajDecyzji;
    }

    public void setRodzajDecyzji(String rodzajDecyzji) {
        this.rodzajDecyzji = rodzajDecyzji;
    }

    public String getUzasadnienieDecyzji() {
        return uzasadnienieDecyzji;
    }

    public void setUzasadnienieDecyzji(String uzasadnienieDecyzji) {
        this.uzasadnienieDecyzji = uzasadnienieDecyzji;
    }
}

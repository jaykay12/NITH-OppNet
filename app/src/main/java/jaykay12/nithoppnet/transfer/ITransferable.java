package jaykay12.nithoppnet.transfer;

import java.io.Serializable;

/**
 * Created by jaykay12 on 31/3/18.
 */

public interface ITransferable extends Serializable {

    int getRequestCode();

    String getRequestType();

    String getData();
}

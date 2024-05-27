package org.maze;

public class SaverFactory {
    public enum SaveType {
        TEXT,
        BINARY,
        IMAGE
    }

    public static Saver CreateSaver(SaveType type) {
        switch (type) {
            default:
            case TEXT: return new TextSaver();
            //case BINARY: return new BinarySaver();
        }
    }
}


/*


public class LoaderFactory {
    public enum LoadType {
        TEXT,
        BINARY,
        IMAGE
    }

    public static Loader CreateLoader(LoadType type) {
        switch (type) {
            default:
            case TEXT: return new TextLoader();
            case BINARY: return new BinaryLoader();
        }
    }
}

 */
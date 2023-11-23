package com.codinggyd.utils;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 序列化和反序列化
 */
public class SerializeUtil {

    public SerializeUtil() {
    }

    public static byte[] serialize(Object object) {
        if (object == null) {
            return null;
        } else {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Throwable var2 = null;

                Object var5;
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    Throwable var4 = null;

                    try {
                        oos.writeObject(object);
                        var5 = baos.toByteArray();
                    } catch (Throwable var30) {
                        var5 = var30;
                        var4 = var30;
                        throw var30;
                    } finally {
                        if (oos != null) {
                            if (var4 != null) {
                                try {
                                    oos.close();
                                } catch (Throwable var29) {
                                    var4.addSuppressed(var29);
                                }
                            } else {
                                oos.close();
                            }
                        }

                    }
                } catch (Throwable var32) {
                    var2 = var32;
                    throw var32;
                } finally {
                    if (baos != null) {
                        if (var2 != null) {
                            try {
                                baos.close();
                            } catch (Throwable var28) {
                                var2.addSuppressed(var28);
                            }
                        } else {
                            baos.close();
                        }
                    }

                }

                return (byte[])var5;
            } catch (Exception var34) {
                return null;
            }
        }
    }

    public static Object unserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                Throwable var2 = null;

                Object var5;
                try {
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    Throwable var4 = null;

                    try {
                        var5 = ois.readObject();
                    } catch (Throwable var30) {
                        var5 = var30;
                        var4 = var30;
                        throw var30;
                    } finally {
                        if (ois != null) {
                            if (var4 != null) {
                                try {
                                    ois.close();
                                } catch (Throwable var29) {
                                    var4.addSuppressed(var29);
                                }
                            } else {
                                ois.close();
                            }
                        }

                    }
                } catch (Throwable var32) {
                    var2 = var32;
                    throw var32;
                } finally {
                    if (bais != null) {
                        if (var2 != null) {
                            try {
                                bais.close();
                            } catch (Throwable var28) {
                                var2.addSuppressed(var28);
                            }
                        } else {
                            bais.close();
                        }
                    }

                }

                return var5;
            } catch (Exception var34) {
                return null;
            }
        }
    }

    public static byte[][] serialize(Object[] objects) {
        if (objects == null) {
            return (byte[][])null;
        } else {
            byte[][] valuesByte = new byte[objects.length][];

            for(int i = 0; i < objects.length; ++i) {
                valuesByte[i] = serialize(objects[i]);
            }

            return valuesByte;
        }
    }

    public static Collection<Object> unserialize(Collection<byte[]> bytes) {
        if (bytes == null) {
            return null;
        } else {
            Set<Object> rets = new HashSet();
            if (bytes.size() > 0) {
                Iterator var2 = bytes.iterator();

                while(var2.hasNext()) {
                    byte[] b = (byte[])var2.next();
                    rets.add(unserialize(b));
                }
            }

            return rets;
        }
    }
}


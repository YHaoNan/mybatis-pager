package top.yudoge.mybatis.interceptors.pager.utils.naming;


public class NamingConvert {
    private final NamingConverter toNaming;
    private final NamingConverter fromNaming;

    public NamingConvert(NamingConverter fromNaming, NamingConverter toNaming) {
        this.fromNaming = fromNaming;
        this.toNaming = toNaming;
    }

    public String convert(String naming) {
        return toNaming.fromStandard(
                fromNaming.toStandard(naming)
        );
    }
}

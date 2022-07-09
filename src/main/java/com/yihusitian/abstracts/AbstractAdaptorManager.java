package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Description
 * @Author LeeHo
 * @Date 2022/7/8 13:54
 */
public class AbstractAdaptorManager {

    private List<AbstractAdaptor> abstractAdaptors;

    public AbstractAdaptorManager(String dirName) {
        abstractAdaptors = Lists.newArrayList();
        abstractAdaptors.add(new LinkSpringerAbstractAdaptor(dirName));
        abstractAdaptors.add(new TandfonlineAbstractAdaptor(dirName));
        abstractAdaptors.add(new PsychiatristAbstractAdaptor(dirName));
        abstractAdaptors.add(new JpsychopatholAbstractAdaptor(dirName));
        abstractAdaptors.add(new SrcdOnlinelibraryWileyAbstractAdaptor(dirName));
        abstractAdaptors.add(new MdpiAbstractAdaptor(dirName));
        abstractAdaptors.add(new CambridgeAbstractAdaptor(dirName));
        abstractAdaptors.add(new SearchProquestAbstractAdaptor(dirName));
        abstractAdaptors.add(new AcademiaAbstractAdaptor(dirName));
        abstractAdaptors.add(new CiteseerxIstPsuAbstractAdaptor(dirName));
        abstractAdaptors.add(new PsycnetApaAbstractAdaptor(dirName));
        abstractAdaptors.add(new NatureAbstractAdaptor(dirName));
        abstractAdaptors.add(new CoreAcAbstractAdaptor(dirName));
        abstractAdaptors.add(new OnlinelibraryWileyAbstractAdaptor(dirName));
        abstractAdaptors.add(new NcbiNlmNihAbstractAdaptor(dirName));
        abstractAdaptors.add(new AcamhOnlinelibraryWileyAbstractAdaptor(dirName));
        abstractAdaptors.add(new FrontiersinAbstractAdaptor(dirName));
        abstractAdaptors.add(new JournalsLwwAbstractAdaptor(dirName));
        abstractAdaptors.add(new JournalsSagepubAbstractAdaptor(dirName));
        abstractAdaptors.add(new SciencedirectAbstractAdaptor(dirName));
        abstractAdaptors.add(new TaylorfrancisAbstractAdaptor(dirName));
        abstractAdaptors.add(new BooksGoogleAbstractAdaptor(dirName));
    }

    /**
     * 获取适配的摘要适配器
     *
     * @param href
     * @return
     */
    public AbstractAdaptor getAbstractAdaptor(String href) {
        if (StrUtil.isEmpty(href)) {
            return null;
        }
        for (AbstractAdaptor abstractAdaptor : abstractAdaptors) {
            if (abstractAdaptor.match(href)) {
                return abstractAdaptor;
            }
        }
        System.out.println("未匹配到摘要适配器: " + href);
        return null;
    }


}
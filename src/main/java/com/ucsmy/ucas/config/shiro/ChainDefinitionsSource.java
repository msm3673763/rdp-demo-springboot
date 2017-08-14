package com.ucsmy.ucas.config.shiro;

import com.ucsmy.ucas.manage.dao.ManagePermissionMapper;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 动态获取资源
 * 
 * @author ucs_zhongtingyuan
 *
 */
public class ChainDefinitionsSource extends ShiroFilterFactoryBean {



	@Autowired
	private ManagePermissionMapper managePermissionMapper;

	// shiro默认的链接定义
	private String filterChainDefinitions;

	public void setFilterChainDefinitions(String filterChainDefinitions) {
		this.filterChainDefinitions = filterChainDefinitions;
	}


	@Override
	public Section getObject() throws Exception {
		Ini ini = new Ini();
		//加载默认的url
		ini.load(filterChainDefinitions);
		Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
		if (section == null || section.isEmpty()) {
			section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
		}

		/*List<ManagePermission> ps = managePermissionMapper.queryPermissionAll();
		Map<String, List<String>> map_ps = new HashMap<>();
		for (ManagePermission p : ps) {
			if (StringUtils.isEmpty(p.getSn()) || StringUtils.isEmpty(p.getUrlAction()))
				continue;
			if (!map_ps.containsKey(p.getUrlAction())) {
				map_ps.put(p.getUrlAction(), new ArrayList<String>());
			}
			map_ps.get(p.getUrlAction()).add(p.getSn());
		}
		for (Map.Entry<String, List<String>> entry : map_ps.entrySet()) {
			String roles = entry.getValue().toString().replace(" ", "");
			section.put(entry.getKey(), "csrf,authc,roles".concat(roles));
		}*/
		section.put("/**", "csrf,authc");
		return section;
	}

	@Override
	public Class<?> getObjectType() {
		return Section.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}


}

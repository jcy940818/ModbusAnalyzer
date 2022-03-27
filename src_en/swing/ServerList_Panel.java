package src_en.swing;

import javax.swing.JPanel;

public class ServerList_Panel extends JPanel {	
	
	private static String serverQuery = 
			"WITH tree_query AS \r\n" + 
			"( SELECT nGroupIndex , nParentIndex , strGroupName \r\n" + 
			", convert(varchar(255), nGroupIndex) sort \r\n" + 
			", convert(varchar(255), strGroupName) depth_fullname \r\n" + 
			"FROM SERVERGROUP WHERE nParentIndex = -1\r\n" + 
			"UNION ALL SELECT B.nGroupIndex , B.nParentIndex , B.strGroupName \r\n" + 
			", convert(varchar(255), convert(nvarchar,C.sort) + ' > ' + convert(varchar(255), B.nGroupIndex)) sort\r\n" + 
			", convert(varchar(255), convert(nvarchar,C.depth_fullname) + ' > ' + convert(varchar(255), B.strGroupName)) depth_fullname \r\n" + 
			"FROM SERVERGROUP B, tree_query C \r\n" + 
			"WHERE B.nParentIndex = C.nGroupIndex) \r\n" + 
			"\r\n" + 
			"select \r\n" + 
			"	replace(c.depth_fullname,'<ROOT>','Devices') as 'groupInfo',	\r\n" + 
			"	a.nServerIndex as 'index',\r\n" + 
			"	f.FACILITY_TYPE as 'facType',\r\n" + 
			"	a.strServerName as 'name',\r\n" + 
			"	f.CONN_METHOD as 'connMethod',\r\n" + 
			"	f.COMM_PROTOCOL as 'commProtocol',\r\n" + 
			"	f.SNMP_MIB as 'snmpProtocol',\r\n" + 
			"	a.SERVER_CONDITION as 'condition'\r\n" + 
			" \r\n" + 
			"from SERVERINFO a inner join SERVERGROUPMAP b on a.nServerIndex=b.nServerIndex\r\n" + 
			"	inner join tree_query c on b.nGroupIndex = c.ngroupIndex\r\n" + 
			"	inner join SERVERINFO_FACILITY f ON a.nServerIndex = f.NODE_INDEX\r\n" + 
			"order by a.nServerIndex\r\n";
	
}

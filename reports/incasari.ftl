<?xml version="1.0" encoding="utf-8"?>

<!--

<#function ssearch node vl_name>
  <#return search(node, vl_name, node.record["field[attribute::name='${vl_name}']"]) >
</#function>


<#function search node vl_name vl_key>

<#list node["value-list"] as vl>
	<#if vl["@name"] = vl_name>
		<#list vl["vl-item"] as vl_item>
			<#if vl_item.value = vl_key>
				<#return vl_item.label>
			</#if>
		</#list>
	</#if>
</#list>

<#return "N/A">

</#function>

-->

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <fo:layout-master-set>
    <fo:simple-page-master master-name="page"
			   page-height="297mm"
			   page-width="210mm"
			   margin-top="5mm"
			   margin-bottom="5mm"
			   margin-left="25mm"
			   margin-right="25mm">
      <fo:region-body margin-top="5mm" margin-bottom="5mm"
		      margin-left="0mm" margin-right="0mm"/>
      <fo:region-before extent="5mm"/>
      <fo:region-after extent="5mm"/>
    </fo:simple-page-master>
  </fo:layout-master-set>

  <fo:page-sequence master-reference="page">
    <!--HEADER-->
    <fo:static-content flow-name="xsl-region-before">
      <fo:block></fo:block>
    </fo:static-content>

    <!--FOOTER-->
    <fo:static-content flow-name="xsl-region-after">
      <fo:block></fo:block>
    </fo:static-content>

    <!--BODY-->
    <fo:flow flow-name="xsl-region-body">


      <!-- main block >> contine tot raportul -->
      <fo:block font-size="10pt" font-family="Times">

	<fo:block font-size="12pt" font-weight="bold" text-align="center" margin-bottom="2cm" margin-top="1cm">
	  Raport incasari 
	  ${doc.response.record["field[@name='toDate']"]}
	  
	</fo:block> 

	<fo:table width="100%">
	  <fo:table-column column-number="1" border-left-style="solid" column-width="26%"/>
	  <fo:table-column column-number="2" border-left-style="solid" column-width="16%"/>
	  <fo:table-column column-number="3" border-left-style="solid" column-width="16%"/>
	  <fo:table-column column-number="4" border-left-style="solid" column-width="16%"/>
	  <fo:table-column column-number="5" border-style="solid" column-width="26%"/>

	  <fo:table-body>
	    <fo:table-row border-style="solid">
	      <fo:table-cell column-number="1" padding="1mm"><fo:block text-align="center">Client</fo:block></fo:table-cell>
	      <fo:table-cell column-number="2" padding="1mm"><fo:block text-align="center">Valoare incasata</fo:block></fo:table-cell>
	      <fo:table-cell column-number="3" padding="1mm"><fo:block text-align="center">Tip factura</fo:block></fo:table-cell>
	      <fo:table-cell column-number="4" padding="1mm"><fo:block text-align="center">Tip produs</fo:block></fo:table-cell>
	      <fo:table-cell column-number="5" padding="1mm"><fo:block text-align="center">Nr document plata/data</fo:block></fo:table-cell>
	    </fo:table-row>
	    
	    <#list doc.response.record["field[@name='payments']/record"] as payment>

	    <fo:table-row border-bottom-style="solid">
	      <fo:table-cell column-number="1" padding="1mm"><fo:block text-align="center">${payment["field[@name='client']"]}</fo:block></fo:table-cell>
	      <fo:table-cell column-number="2" padding="1mm"><fo:block text-align="right">${payment["field[@name='amount']"]?number?string("#,##0.00")}</fo:block></fo:table-cell>
	      <fo:table-cell column-number="3" padding="1mm"><fo:block text-align="center">${payment["field[@name='tipFactura']"]}</fo:block></fo:table-cell>
	      <fo:table-cell column-number="4" padding="1mm"><fo:block text-align="center">UA</fo:block></fo:table-cell>
	      <fo:table-cell column-number="5" padding="1mm"><fo:block text-align="center">${payment["field[@name='document']"]} / ${payment["field[@name='data']"]}</fo:block></fo:table-cell>	      
	    </fo:table-row>

	    </#list>
  
	    <fo:table-row border-bottom-style="solid">
	      <fo:table-cell column-number="1" padding="1mm"><fo:block text-align="center" font-weight="bold">Total</fo:block></fo:table-cell>
	      <fo:table-cell column-number="2" padding="1mm"><fo:block text-align="right" font-weight="bold">${doc.response.record["field[@name='total']"]?number?string("#,##0.00")}</fo:block></fo:table-cell>
	      <fo:table-cell column-number="3" padding="1mm"><fo:block text-align="center"></fo:block></fo:table-cell>
	      <fo:table-cell column-number="4" padding="1mm"><fo:block text-align="center"></fo:block></fo:table-cell>
	      <fo:table-cell column-number="5" padding="1mm"><fo:block text-align="center"></fo:block></fo:table-cell>
	    </fo:table-row>
	  </fo:table-body>
	</fo:table>


	<fo:table width="100%" space-before="5mm">
	  <fo:table-column column-number="1" border-left-style="solid" column-width="20%"/>
	  <fo:table-column column-number="2" border-left-style="solid" column-width="20%"/>
	  <fo:table-column column-number="3" border-left-style="solid" column-width="20%"/>
	  <fo:table-column column-number="4" border-left-style="solid" column-width="20%"/>
	  <fo:table-column column-number="5" border-style="solid" column-width="20%"/>

	  <fo:table-body>
	    <fo:table-row border-style="solid">
	      <fo:table-cell column-number="1" padding="1mm" number-rows-spanned="2"><fo:block text-align="center">RAPORT EXPEDITIE /ZI</fo:block></fo:table-cell>
	      <fo:table-cell column-number="2" padding="1mm"><fo:block text-align="center">Valoare incasata</fo:block></fo:table-cell>
	      <fo:table-cell column-number="3" padding="1mm"><fo:block text-align="center">BUC Incasate</fo:block></fo:table-cell>
	      <fo:table-cell column-number="4" padding="1mm"><fo:block text-align="center">BUC Neincasate</fo:block></fo:table-cell>
	      <fo:table-cell column-number="5" padding="1mm"><fo:block text-align="center">BUC Rate</fo:block></fo:table-cell>
	    </fo:table-row>
	    <fo:table-row border-style="solid">
	      <fo:table-cell column-number="2" padding="1mm"><fo:block text-align="center">${doc.response.record["field[@name='valoare']"]?number?string("#,##0.00")}</fo:block></fo:table-cell>
	      <fo:table-cell column-number="3" padding="1mm"><fo:block text-align="center">${doc.response.record["field[@name='bucIncasate']"]}</fo:block></fo:table-cell>
	      <fo:table-cell column-number="4" padding="1mm"><fo:block text-align="center">${doc.response.record["field[@name='bucNeincasate']"]}</fo:block></fo:table-cell>
	      <fo:table-cell column-number="5" padding="1mm"><fo:block text-align="center">${doc.response.record["field[@name='bucRate']"]}</fo:block></fo:table-cell>
	    </fo:table-row>
	  </fo:table-body>
	</fo:table>

	<fo:table width="100%" space-before="5mm">
	  <fo:table-column column-number="1" border-left-style="solid" column-width="20%"/>
	  <fo:table-column column-number="2" border-style="solid" column-width="80%"/>

	  <fo:table-body>
	    <fo:table-row border-style="solid">
	      <fo:table-cell column-number="1" padding="1mm"><fo:block text-align="center"> TOTAL INCASAT /ZI</fo:block></fo:table-cell>
	      <fo:table-cell column-number="2" padding="1mm"><fo:block text-align="left">${doc.response.record["field[@name='totalZi']"]?number?string("#,##0.00")}</fo:block></fo:table-cell>
	    </fo:table-row>
	  </fo:table-body>
	</fo:table>

	<fo:table width="100%" space-before="5mm">
	  <fo:table-column column-number="1" border-left-style="solid" column-width="20%"/>
	  <fo:table-column column-number="2" border-style="solid" column-width="80%"/>

	  <fo:table-body>
	    <fo:table-row border-style="solid">
	      <fo:table-cell column-number="1" padding="1mm"><fo:block text-align="center"> TOTAL CUMULAT de la ${doc.response.record["field[@name='fromDate']"]}</fo:block></fo:table-cell>
	      <fo:table-cell column-number="2" padding="1mm"><fo:block text-align="left">${doc.response.record["field[@name='totalZi']"]?number?string("#,##0.00")}</fo:block></fo:table-cell>
	    </fo:table-row>
	  </fo:table-body>
	</fo:table>

      <fo:table width="100%" space-before="5mm">
	  <fo:table-column column-number="1" border-left-style="solid" column-width="25%"/>
	  <fo:table-column column-number="2" border-left-style="solid" column-width="25%"/>
	  <fo:table-column column-number="3" border-left-style="solid" column-width="25%"/>
	  <fo:table-column column-number="4" border-style="solid" column-width="25%"/>

	  <fo:table-body>
	    <fo:table-row border-style="solid">
	      <fo:table-cell column-number="1" padding="1mm"><fo:block text-align="center">USI CUMULATE de la ${doc.response.record["field[@name='fromDate']"]}</fo:block></fo:table-cell>
	      <fo:table-cell column-number="2" padding="1mm"><fo:block text-align="center">BUC Incasate</fo:block></fo:table-cell>
	      <fo:table-cell column-number="3" padding="1mm"><fo:block text-align="center">BUC Neincasate</fo:block></fo:table-cell>
	      <fo:table-cell column-number="4" padding="1mm"><fo:block text-align="center">BUC Rate</fo:block></fo:table-cell>
	    </fo:table-row>

	    <fo:table-row border-style="solid">
	      <fo:table-cell column-number="1" padding="1mm"><fo:block text-align="center">${doc.response.record["field[@name='totalBuc']"]}</fo:block></fo:table-cell>
	      <fo:table-cell column-number="2" padding="1mm"><fo:block text-align="center">${doc.response.record["field[@name='totalBucIncasate']"]}</fo:block></fo:table-cell>
	      <fo:table-cell column-number="3" padding="1mm"><fo:block text-align="center">${doc.response.record["field[@name='totalBucNeincasate']"]}</fo:block></fo:table-cell>
	      <fo:table-cell column-number="4" padding="1mm"><fo:block text-align="center">${doc.response.record["field[@name='totalBucRate']"]}</fo:block></fo:table-cell>
	    </fo:table-row>
	  </fo:table-body>
      </fo:table>
      
    </fo:block>
    <!-- end main block -->

    </fo:flow>
    <!--/BODY-->
  </fo:page-sequence>
</fo:root>
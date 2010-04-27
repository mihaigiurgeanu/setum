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

<#assign currency = doc.response.record["field[@name='proformaCurrency']"]>
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


      <fo:block font-size="10pt" font-family="Times">
	<fo:table width="100%">
	  <fo:table-column column-number="1" column-width="45%"/>
	  <fo:table-column column-number="1" column-width="25%"/>
	  <fo:table-column column-number="1" column-width="30%"/>
	  
	  <fo:table-body>
	    <fo:table-row>
	      <fo:table-cell column-number="1">
		<fo:block>Furnizor: S.C. SETUM S.A.</fo:block>
		<fo:block>Nr.ord.registru com/an: J40/314/1991</fo:block>
		<fo:block>Sediul: Bucuresti, Sector 6</fo:block>
		<fo:block> B-dul Preciziei nr. 32</fo:block>
		<fo:block>Cont: RO94 RNCB 0077 0084 5261 0001</fo:block>
		<fo:block>Banca: B.C.R. Sucrusala Sector 6</fo:block>
		<fo:block>Tel: 021.316.18.56</fo:block>
		<fo:block>Fax: 021.316.05.90</fo:block>
		<fo:block>contractari@setumsa.ro</fo:block>
		<fo:block>desfacere@setumsa.ro</fo:block>
	      </fo:table-cell>

	      <fo:table-cell column-number="2" padding-top="20mm">
		<fo:block text-align="center">FACTURA</fo:block>
		<fo:block text-align="center">PROFORMA</fo:block>
	      </fo:table-cell>

	      <fo:table-cell column-number="3">
		<fo:block>Cumparator: ${doc["response/record/field[@name='clientName']"]}</fo:block>
		<fo:block>Cu sediul in: 
		${doc["response/record/field[@name='clientCity']"]},
		${doc["response/record/field[@name='clientAddress']"]}
		</fo:block>
		<fo:block>Registrul Comertului nr.
		<!--
		    <#if doc["response/record/field[@name='clientIsCompany']"] == "true">
		    <#assign regCom = doc["response/record/field[@name='clientRegCom']"]>
		    <#assign cf = doc["response/record/field[@name='clientCompanyCode']"]>
		    <#else>
		    <#assign regCom = "">
		    <#assign cf = "">
		    </#if>
		-->
		${regCom}
		</fo:block>
		<fo:block>Cod fiscal: ${cf}</fo:block>
	      </fo:table-cell>
	    </fo:table-row>
	    <fo:table-row>
	      <fo:table-cell number-columns-spanned="3">
		<fo:block text-align="center">Nr. Facturii: 
		${doc["response/record/field[@name='proformaNumber']"]}</fo:block>
		<fo:block text-align="center">Data: 
		${doc["response/record/field[@name='proformaDate']"]}
		</fo:block>
	      </fo:table-cell>
	    </fo:table-row>
	  </fo:table-body>
	</fo:table>

	<!--Corpul facturii-->
	<fo:table width="100%">
	  <fo:table-column column-number="1" border-style="solid" column-width="50%"/>
	  <fo:table-column column-number="2" border-style="solid" column-width="7%"/>
	  <fo:table-column column-number="3" border-style="solid" column-width="7%"/>
	  <fo:table-column column-number="4" border-style="solid" column-width="18%"/>
	  <fo:table-column column-number="5" border-style="solid" column-width="18%"/>
	  
	  <fo:table-body>
	    <fo:table-row border-bottom-style="solid">
	      <fo:table-cell column-number="1" padding="1mm">
		<fo:block text-align="center">Denumirea produselor</fo:block>
		<fo:block text-align="center">sau a serviciilor</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2" padding="1mm">
		<fo:block text-align="center">UM</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="3" padding="1mm">
		<fo:block text-align="center">Cant</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="4" padding="1mm">
		<fo:block text-align="center">Pretul unitar (fara TVA)</fo:block>
		<fo:block text-align="center">${currency}</fo:block>

	      </fo:table-cell>
	      <fo:table-cell column-number="5" padding="1mm">
		<fo:block text-align="center">Valoarea (fara TVA)</fo:block>
		<fo:block text-align="center">${currency}</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row height="12cm">
	      <fo:table-cell column-number="1" padding="1mm">
		<fo:block>
		  ${ssearch(doc.response, "proformaRole")}
		  <!--
		      <#if doc.response.record["field[@name='proformaUsePercent']"] == "true">
		         <#assign val = doc.response.record["field[@name='proformaPercent']"]>
			<#assign valUM = "%">
		      <#else>
		        <#assign val = doc.response.record["field[@name='proformaAmountCurrency']"]?number?string("#,##0.00")>
			<#assign valUM = " ${currency}">
		      </#if>
		  -->
		  ${val}${valUM}
		  -
		  Contr. Nr. ${doc.response.record["field[@name='proformaContract']"]}
		  -
		  obiectiv "${doc.response.record["field[@name='proformaObiectiv']"]}"
		</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2" padding="1mm">
		<fo:block></fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="3" padding="1mm">
		<fo:block></fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="4" padding="1mm">
		<fo:block></fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="5" padding="1mm">
		<fo:block text-align="right">${doc.response.record["field[@name='proformaAmountCurrency']"]?number?string("#,##0.00")}</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row>
	      <fo:table-cell column-number="1" padding="1mm">
		<fo:block text-align="right">TOTAL fara TVA</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2">
		<fo:block></fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="3">
		<fo:block></fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="4">
		<fo:block></fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="5" padding="1mm">
		<fo:block text-align="right">${doc.response.record["field[@name='proformaAmountCurrency']"]?number?string("#,##0.00")} ${currency}</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row>
	      <fo:table-cell column-number="1" padding="1mm">
		<fo:block text-align="right">TVA 19%</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2">
		<fo:block></fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="3">
		<fo:block></fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="4">
		<fo:block></fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="5" padding="1mm">
		<fo:block text-align="right">${doc.response.record["field[@name='proformaTaxCurrency']"]?number?string("#,##0.00")} ${currency}</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row>
	      <fo:table-cell column-number="1" padding="1mm">
		<fo:block text-align="right">TOTAL cu TVA</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2">
		<fo:block></fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="3">
		<fo:block></fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="4">
		<fo:block></fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="5" padding="1mm">
		<fo:block text-align="right">${doc.response.record["field[@name='proformaTotalCurrency']"]?number?string("#,##0.00")} ${currency}</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	  </fo:table-body>
	</fo:table>

	<fo:block>
	  ${doc.response.record["field[@name='proformaComment']"]}
	</fo:block>

      </fo:block>


    </fo:flow>
    <!--/BODY-->
  </fo:page-sequence>
</fo:root>
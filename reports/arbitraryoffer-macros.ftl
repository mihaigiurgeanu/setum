<#-- arbitraryoffer-macros.ftl -->
<#-- macro used to generate the report -->



<#-- parcurge oferta si grupeaza liniile dupa caracteristici -->
<#function group_line_offers doc>
  <#-- incep cu usile metalice -->
  <#assign usilines = sel_product(doc, 9990)>
  <#assign usi = {} >
  <#list usilines as usa>
    <#local groupingCode>
    <#assign groupingCode = usa["field[attribute::name='product']/record/field[attribute::name='groupingCode']"] >
    <#assign usi[groupingCode] = (usi[groupingCode]!) + [usa] >
  </#list>


  <#-- sisteme -->
  <#assign sisteme = []>
  <#assign sisteme = sisteme + sel_product(doc, 9998)>
  <#assign sisteme = sisteme + sel_product(doc, 9997)>
  <#assign sisteme = sisteme + sel_product(doc, 9996)>
  <#assign sisteme = sisteme + sel_product(doc, 9995)>
  <#assign sisteme = sisteme + sel_product(doc, 9994)>
  <#assign sisteme = sisteme + sel_product(doc, 10000)>
  <#assign sisteme = sisteme + sel_product(doc, 10001)>
  <#assign sisteme = sisteme + sel_product(doc, 10002)>
  <#assign sisteme = sisteme + sel_product(doc, 10003)>
  <#assign sisteme = sisteme + sel_product(doc, 10004)>
  <#assign sisteme = sisteme + sel_product(doc, 10005)>
  <#assign sisteme = sisteme + sel_product(doc, 10006)>
  
  <#return {"usi":usi, "sisteme":sisteme} >
</#function>

<#-- select the list of products with a given product category -->
<#-- returneaza liniile de oferta -->
<#function sel_product doc categId>
  <#assign records=doc["response/record/field[attribute::name='lines']/record[field[attribute::name='product']/record[field[attribute::name='category.id']=${categId}]]"]>
  <#return records>
</#function>

<#macro "record">
   <#switch .node["child::field[attribute::name='category.id']/child::text()"]?number>
   <#case 9990>
   ${search(.node?parent, "material", .node["child::field[attribute::name='material']"])?cap_first}, 
		Nr canate: ${prop(.node, "k")},
                <!--Nr canate:${.node["child::field[attribute::name='k']"]},-->
		Lg = ${.node["child::field[attribute::name='lg']"]},
		Hg = ${.node["child::field[attribute::name='hg']"]},
                Le = ${.node["child::field[attribute::name='le']"]},
                He = ${.node["child::field[attribute::name='he']"]},
		L util = ${.node["child::field[attribute::name='lUtil']"]},
		H util = ${.node["child::field[attribute::name='hUtil']"]},
		L foaie = ${.node["child::field[attribute::name='lFoaie']"]},
		H foaie = ${.node["child::field[attribute::name='hFoaie']"]},
		Foaie interior: ${search(.node?parent, "intFoil", .node["child::field[attribute::name='intFoil']"])?cap_first},
		Foaie exterior: ${search(.node?parent, "extFoil", .node["child::field[attribute::name='extFoil']"])?cap_first},
		Deschidere spre: ${search(.node?parent, "openingDir", .node["child::field[attribute::name='openingDir']"])?cap_first},
		Deschiderea in: ${search(.node?parent, "openingSide", .node["child::field[attribute::name='openingSide']"])?cap_first},
		Pozitionare foaie: ${search(.node?parent, "foilPosition", .node["child::field[attribute::name='foilPosition']"])?cap_first},
		Tip toc: ${search(.node?parent, "frameType", .node["child::field[attribute::name='frameType']"])?cap_first},
		L toc = ${.node["child::field[attribute::name='lFrame']"]},
		b toc = ${.node["child::field[attribute::name='bFrame']"]},
		c toc = ${.node["child::field[attribute::name='cFrame']"]},
		Tip prag: ${search(.node?parent, "tresholdType", .node["child::field[attribute::name='tresholdType']"])?cap_first},
		L prag = ${.node["child::field[attribute::name='lTreshold']"]},
		H prag = ${.node["child::field[attribute::name='hTreshold']"]},
		C prag = ${.node["child::field[attribute::name='cTreshold']"]},

		<@system name="broasca">Broasca</@system>
		<!--
		<#if .node["child::field[attribute::name='broascaBuc']"]?number &gt; 0>
		Broasca ${search(.node?parent,"broascaId",.node["child::field[attribute::name='broascaId']"])} - ${.node["child::field[attribute::name='broascaBuc']"]} buc.
		</#if>
		-->
		<#if .node["child::field[attribute::name='cilindruBuc']"]?number &gt; 0>
		Cilindru ${search(.node?parent,"cilindruId",.node["child::field[attribute::name='cilindruId']"])} - ${.node["child::field[attribute::name='cilindruBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='copiatCheieBuc']"]?number &gt; 0>
		${search(.node?parent,"copiatCheieId",.node["child::field[attribute::name='copiatCheieId']"])} - ${.node["child::field[attribute::name='copiatCheieBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='vizorBuc']"]?number &gt; 0>
		Vizor ${search(.node?parent,"vizorId",.node["child::field[attribute::name='vizorId']"])} - ${.node["child::field[attribute::name='vizorBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='sildBuc']"]?number &gt; 0>
		Sild ${search(.node?parent,"sildId",.node["child::field[attribute::name='sildId']"])} - ${.node["child::field[attribute::name='sildBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='rozetaBuc']"]?number &gt; 0>
		Rozeta ${search(.node?parent,"rozetaId",.node["child::field[attribute::name='rozetaId']"])} - ${.node["child::field[attribute::name='rozetaBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='manerBuc']"]?number &gt; 0>
		Maner ${search(.node?parent,"manerId",.node["child::field[attribute::name='manerId']"])} - ${.node["child::field[attribute::name='manerBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='yalla1Buc']"]?number &gt; 0>
		Yalla ${search(.node?parent,"yalla1Id",.node["child::field[attribute::name='yalla1Id']"])} - ${.node["child::field[attribute::name='yalla1Buc']"]} buc.
		</#if>

		<#if .node["child::field[attribute::name='yalla2Buc']"]?number &gt; 0>
		Yalla ${search(.node?parent,"yalla2Id",.node["child::field[attribute::name='yalla2Id']"])} - ${.node["child::field[attribute::name='yalla2Buc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='baraAntipanicaBuc']"]?number &gt; 0>
		Bara antipanica ${search(.node?parent,"baraAntipanicaId",.node["child::field[attribute::name='baraAntipanicaId']"])} - ${.node["child::field[attribute::name='baraAntipanicaBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='manerSemicilindruBuc']"]?number &gt; 0>
		Maner semicilindru ${search(.node?parent,"manerSemicilindruId",.node["child::field[attribute::name='manerSemicilindruId']"])} - ${.node["child::field[attribute::name='manerSemicilindruBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='selectorOrdineBuc']"]?number &gt; 0>
		Selector ordine ${search(.node?parent,"selectorOrdineId",.node["child::field[attribute::name='selectorOrdineId']"])} - ${.node["child::field[attribute::name='selectorOrdineBuc']"]} buc.
		</#if>

		<#if .node["child::field[attribute::name='amortizorBuc']"]?number &gt; 0>
		Amortizor ${search(.node?parent,"amortizorId",.node["child::field[attribute::name='amortizorId']"])} - ${.node["child::field[attribute::name='amortizorBuc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='alteSisteme1Buc']"]?number &gt; 0>
		${search(.node?parent,"alteSisteme1Id",.node["child::field[attribute::name='alteSisteme1Id']"])} - ${.node["child::field[attribute::name='alteSisteme1Buc']"]} buc.
		</#if>
		<#if .node["child::field[attribute::name='alteSisteme2Buc']"]?number &gt; 0>
		${search(.node?parent,"alteSisteme2Id",.node["child::field[attribute::name='alteSisteme2Id']"])} - ${.node["child::field[attribute::name='alteSisteme2Buc']"]} buc.
		</#if>

    <#if .node["child::field[attribute::name='intFinisajBlatId']"]?number != 0>
    Finisaj int. blat: ${.node["child::field[attribute::name='intFinisajBlat']"]}.
    </#if>
    <#if .node["child::field[attribute::name='intFinisajTocId']"]?number != 0>
    Finisaj int. toc: ${.node["child::field[attribute::name='intFinisajToc']"]}.
    </#if>
    <#if .node["child::field[attribute::name='intFinisajGrilajId']"]?number != 0>
    Finisaj int. grilaj: ${.node["child::field[attribute::name='intFinisajGrilaj']"]}.
    </#if>
    <#if .node["child::field[attribute::name='intFinisajBlatId']"]?number != 0>
    Finisaj int. fereastra: ${.node["child::field[attribute::name='intFinisajBlat']"]}.
    </#if>
    <#if .node["child::field[attribute::name='intFinisajSupraluminaId']"]?number != 0>
    Finisaj int. supralumina: ${.node["child::field[attribute::name='intFinisajSupralumina']"]}.
    </#if>
    <#if .node["child::field[attribute::name='intFinisajPanouLateralId']"]?number != 0>
    Finisaj int. panou: ${.node["child::field[attribute::name='intFinisajPanouLateral']"]}.
    </#if>

    <#if .node["child::field[attribute::name='intFinisajBlatId']"]?number != 0>
    Finisaj int. blat: ${.node["child::field[attribute::name='intFinisajBlat']"]}.
    </#if>
    <#if .node["child::field[attribute::name='extFinisajTocId']"]?number != 0>
    Finisaj ext. toc: ${.node["child::field[attribute::name='extFinisajToc']"]}.
    </#if>
    <#if .node["child::field[attribute::name='extFinisajGrilajId']"]?number != 0>
    Finisaj ext. grilaj: ${.node["child::field[attribute::name='extFinisajGrilaj']"]}.
    </#if>
    <#if .node["child::field[attribute::name='extFinisajBlatId']"]?number != 0>
    Finisaj ext. fereastra: ${.node["child::field[attribute::name='extFinisajBlat']"]}.
    </#if>
    <#if .node["child::field[attribute::name='extFinisajSupraluminaId']"]?number != 0>
    Finisaj ext. supralumina: ${.node["child::field[attribute::name='extFinisajSupralumina']"]}.
    </#if>
    <#if .node["child::field[attribute::name='extFinisajPanouLateralId']"]?number != 0>
    Finisaj ext. panou: ${.node["child::field[attribute::name='extFinisajPanouLateral']"]}.
    </#if>


    <#list .node["child::field[attribute::name='parts']/child::record/child::field[attribute::name='part']/child::record"] as record>
    <fo:block>
    <#visit record>
    </fo:block>
    </#list>

  <#break>

  <#case 9980>
  Panou lateral ${.node["child::field[attribute::name='lpl']"]} mm X ${.node["child::field[attribute::name='hpl']"]} mm
  avand ${.node["child::field[attribute::name='cells']"]} celule -
  ${.node["child::field[attribute::name='quantity']"]} buc.
  <#break>

  <#case 9981>
  Supralumina ${.node["child::field[attribute::name='ls']"]} mm X ${.node["child::field[attribute::name='hs']"]} mm
  avand ${.node["child::field[attribute::name='cells']"]} celule
  ${.node["child::field[attribute::name='quantity']"]} buc.
  <#break>

  <#case 9982>
  Gauri aerisire cu diametrul ${.node["child::field[attribute::name='diametru']"]} mm,
  pasul ${.node["child::field[attribute::name='pas']"]} mm
  ${.node["child::field[attribute::name='nrRanduri']"]} randuri.
  <#break>

  <#case 9983>
  Grila ventilatie ${.node["child::field[attribute::name='lgv']"]} mm X ${.node["child::field[attribute::name='hgv']"]} mm
  ${.node["child::field[attribute::name='quantity']"]} buc.
  <#break>
  
  <#case 9989>
  Fereastra ${.node["child::field[attribute::name='lf']"]} mm X ${.node["child::field[attribute::name='hf']"]} mm
  ${.node["child::field[attribute::name='quantity']"]} buc.
  <#break>
  
  <#default>
  ${.node["child::field[attribute::name='category.name']"]?cap_first}
  ${.node["child::field[attribute::name='code']"]}:
  <@partslisting .node/>
  
  </#switch>

</#macro>




<#macro "partslisting" node>

<#local records = node["child::field[attribute::name='parts']/child::record/child::field[attribute::name='part']/child::record"]>

<#if records?size &gt; 0>
${record["child::field[attribute::name='category.name']"]} 
${record["child::field[attribute::name='code']"]};
</#if>
</#macro>



<#-- search a value-list for a value coresponding to a value-id -->
<#function search node vl_name vl_key>


<#-- 
Searches the node for a child value-list with the given name, and in the
value list searches a child with the give key (value) and returns the associated label
-->


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






<#-- afiseaza un sistem dintr-o usa -->
<#macro system name>
  <#if .node["child::field[attribute::name='${name}Id']"]?number &gt; 0>
  <#nested> ${search(.node?parent, "${name}Id", .node["child::field[attribute::name='${name}Id']"])} - ${.node["child::field[attribute::name='${name}Buc']"]} buc.
  </#if>
</#macro>

<#-- selecteaza un atribut al nodului dat -->
<#function prop node name>
  <#return node["child::field[attribute::name='${name}']"]>
</#function>
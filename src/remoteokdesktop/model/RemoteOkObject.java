package remoteokdesktop.model;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoteOkObject {
    private Long id;
    private Long epoch;
    private Date date;
    private String company;
    private String companyLogoUrl;
    private List<String> tags;
    private String slug;
    private String logoUrl;
    private String description;
    private String url;
}

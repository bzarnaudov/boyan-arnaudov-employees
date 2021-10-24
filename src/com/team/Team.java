package com.team;

public class Team {
    private int employeeId1;
    private int employeeId2;
    private int projectId;
    private int daysWorkingTogether;

    public Team(int employeeId1, int employeeId2, int projectId, int daysWorkingTogether) {
        this.employeeId1 = employeeId1;
        this.employeeId2 = employeeId2;
        this.projectId = projectId;
        this.daysWorkingTogether = daysWorkingTogether;
    }

    public int getEmployeeId1() {
        return employeeId1;
    }

    public int getEmployeeId2() {
        return employeeId2;
    }

    public int getProjectId() {
        return projectId;
    }

    public int getDaysWorkingTogether() {
        return daysWorkingTogether;
    }

    public void setEmployeeId1(int employeeId1) {
        this.employeeId1 = employeeId1;
    }

    public void setEmployeeId2(int employeeId2) {
        this.employeeId2 = employeeId2;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setDaysWorkingTogether(int daysWorkingTogether) {
        this.daysWorkingTogether = daysWorkingTogether;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        if (employeeId1 != team.employeeId1) return false;
        if (employeeId2 != team.employeeId2) return false;
        if (projectId != team.projectId) return false;
        return daysWorkingTogether == team.daysWorkingTogether;
    }

    @Override
    public int hashCode() {
        int result = employeeId1;
        result = 31 * result + employeeId2;
        result = 31 * result + projectId;
        result = 31 * result + daysWorkingTogether;
        return result;
    }

    @Override
    public String toString() {
        return "Team{" +
                "employeeId1=" + employeeId1 +
                ", employeeId2=" + employeeId2 +
                ", projectId=" + projectId +
                ", daysWorkingTogether=" + daysWorkingTogether +
                '}';
    }
}
